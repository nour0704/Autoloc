# Atelier 6 : Patrons de Conception GOF et Principes SOLID

Ce document présente l'analyse critique de l'architecture initiale (`AutoLocManager`), les réponses aux questions conceptuelles de l'Atelier 6, ainsi que la refactorisation complète mise en œuvre avec les patrons de conception GOF (**Strategy**, **Factory Method**, **Observer**) et le respect des principes **SOLID**.

---

## 1. Partie 1 : Analyse de la classe problématique (`AutoLocManager`)

### 1.1 Classe initiale v0 : `AutoLocManager`
Dans la version initiale `AutoLocManager`, toutes les opérations métier étaient centralisées dans une seule classe :
- Calcul de tarification par chaîne conditionnelle (`if-else if`).
- Gestion et affichage du contrat.
- Déclenchement du paiement.
- Envoi de notifications par Email et par SMS.
- Persistance du contrat.

Cette classe constitue un anti-patron de conception bien connu : la **"God Class"** (ou *Blob*).

---

### 1.2 Réponses aux questions d'analyse

#### Question 1 : Combien de responsabilités possède la classe AutoLocManager ?
**Réponse :**
La classe `AutoLocManager` concentre au minimum **5 responsabilités distinctes** :
1. **Calcul du prix de la location** : Gestion des règles de tarification (durée, coefficients week-end, longue durée, standard).
2. **Création et gestion du cycle de vie du contrat** : Gestion logique de la création d'un contrat de location.
3. **Gestion des paiements** : Appel et enregistrement du paiement (`enregistrerPaiement()`).
4. **Gestion des notifications multicanales** : Formatage et envoi des notifications aux clients par Email (`envoyerEmail`) et par SMS (`envoyerSMS`).
5. **Persistance des données** : Sauvegarde et stockage du contrat dans le système de persistance (`sauvegarderContrat()`).

---

#### Question 2 : Quel principe SOLID n’est pas respecté ?
**Réponse :**
Deux principes majeurs sont directement violés :
1. **SRP (Single Responsibility Principle - Principe de Responsabilité Unique)** :
   - *Règle* : Une classe ne doit avoir qu'une seule raison de changer.
   - *Constat* : `AutoLocManager` possède 5 raisons différentes d'être modifiée (changement du barème tarifaire, ajout d'une passerelle de paiement, modification du fournisseur d'email, changement de format de contrat, modification de la persistance).
2. **DIP (Dependency Inversion Principle - Principe d'Inversion des Dépendances)** :
   - Les modules de haut niveau ne doivent pas dépendre des modules de bas niveau. Les deux doivent dépendre d'abstractions. Ici, la logique de haut niveau est fortement couplée aux détails d'implémentation (hardcodés).

---

#### Question 3 : Que se passe-t-il si AutoLoc ajoute un nouveau type de réservation (ex: Pleinesaison) ?
**Réponse :**
Si AutoLoc souhaite ajouter un nouveau type de réservation comme `Pleinesaison` :
1. Il est obligatoire de **modifier directement le code source existant** de la méthode `louerVehicule()` dans `AutoLocManager`.
2. Il faut ajouter une nouvelle clause conditionnelle `else if (typeReservation.equals("Pleinesaison")) { ... }`.
3. Cette modification directe d'une méthode existante :
   - Risque d'introduire des régressions sur les autres cas déjà testés (Standard, Weekend, LongDuree).
   - Augmente la complexité cyclomatique du code.
   - Rend les tests unitaires plus lourds et fragiles.

**Donc, violation de quel principe :**
- **OCP (Open/Closed Principle - Principe Ouvert/Fermé)** :
  - *Règle* : Une entité logicielle (classe, module, fonction) doit être **ouverte à l'extension**, mais **fermée à la modification**.
  - Dans la version v0, pour étendre le comportement (ajouter un tarif), on est contraint d'altérer la classe existante au lieu de créer une nouvelle classe sans toucher au code existant.

---

## 2. Partie 2 : Refactoring avec le patron Strategy

### 2.1 Objectif
Déléguer la logique de calcul de prix à une famille d'algorithmes interchangeables encapsulés derrière une abstraction commune.

### 2.2 Interface et Implémentations
- **`ITarificationStrategy`** : Interface définissant la signature commune :
  - `BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation)`
  - `String getCode()`
- **`TarifStandardStrategy`** (`"STANDARD"`) : Tarif journalier $\times$ nombre de jours.
- **`TarifWeekendStrategy`** (`"WEEKEND"`) : Remise de 10% appliquée au tarif de base (coefficient `0.90`).
- **`TarifLongueDureeStrategy`** (`"LONGUE_DUREE"`) : Remise de 20% pour les séjours $\ge 7$ jours (coefficient `0.80`).

### 2.3 Bénéfices SOLID
- **OCP respecté** : Pour ajouter un tarif `Pleinesaison`, il suffit de créer une nouvelle classe `TarifPleineSaisonStrategy implements ITarificationStrategy` sans modifier aucune ligne des classes existantes.
- **SRP respecté** : Chaque stratégie est uniquement responsable de son propre calcul mathématique/commercial.

---

## 3. Partie 3 : Factory Pattern

### 3.1 `TarificationStrategyFactory`
Résout dynamiquement la stratégie de tarification appropriée en fonction des attributs de la réservation (durée, dates début/fin, détection de fin de semaine via `estWeekend`) :
- Utilise l'injection automatique par Spring de toutes les classes implémentant `ITarificationStrategy` dans une `Map<String, ITarificationStrategy>` indexée par leur code (`"STANDARD"`, `"WEEKEND"`, `"LONGUE_DUREE"`).
- Découple totalement le code client de l'instanciation des stratégies.

### 3.2 `NotificationFactory`
Implémente le patron **Factory Method** pour instancier ou fournir l'observateur correspondant au canal souhaité (`EMAIL` ou `SMS`) :
- Méthode `creerObservateur(String canal)` retournant un `IReservationObserver`.
- Masque les détails d'implémentation concrets (`EmailNotificationObserver`, `SmsNotificationObserver`) au client appelant.

---

## 4. Partie 4 : Observer Pattern

### 4.1 Objectif
Notifier automatiquement et de manière découplée les différents canaux et services lors des changements d'état du cycle de vie d'une réservation (création / confirmation, annulation).

### 4.2 Composants
1. **Interface `IReservationObserver`** :
   - `onReservationConfirmee(Reservation reservation)`
   - `onReservationAnnulee(Reservation reservation)`
2. **Sujet Observable `ReservationSubject`** :
   - Injecte automatiquement la liste de tous les observateurs Spring (`List<IReservationObserver>`).
   - Maintient la liste des observateurs et notifie via `notifierConfirmation()` et `notifierAnnulation()`.
3. **Observateurs Concrets** :
   - **`EmailNotificationObserver`** : Journalise `[EMAIL] Confirmation envoyee au client ...` et `[EMAIL] Annulation notifiee ...`.
   - **`SmsNotificationObserver`** : Journalise `[SMS] Confirmation envoyee au ...` et `[SMS] Annulation notifiee ...`.

---

## 5. Synthèse des Patrons et Principes

| Principe / Patron | Implémentation AutoLoc | Bénéfice obtenu |
|---|---|---|
| **SRP** | Séparation Tarification / Notifications / Workflow | Chaque composant a une responsabilité claire et unique |
| **OCP** | Nouvelles stratégies et nouveaux observateurs sans toucher au code existant | Extensibilité totale sans risque de régression |
| **DIP** | Injection d'abstractions (`ITarificationStrategy`, `IReservationObserver`) | Découplage fort, testabilité maximale avec mocks |
| **Strategy (GoF)** | `ITarificationStrategy` et ses classes concrètes | Algorithmes de prix interchangeables au runtime |
| **Factory (GoF)** | `TarificationStrategyFactory`, `NotificationFactory` | Centralisation de l'instanciation et sélection dynamique |
| **Observer (GoF)** | `ReservationSubject`, `IReservationObserver` | Architecture réactive découplée pour les événements métier |
