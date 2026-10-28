# Note d'Atelier 4 : Inversion de Contrôle, Injection de Dépendances et Couche Service

---

## Section A. Modes d'injection comparés

| Critère | Injection par constructeur | Injection par setter | Injection par attribut (`@Autowired`) |
| :--- | :---: | :---: | :---: |
| **Champ `final` possible ?** | **Oui** (initialisé dans le constructeur) | **Non** (modifié ultérieurement) | **Non** (injecté par réflexion après instanciation) |
| **Dépendances visibles dans la signature ?** | **Oui** (explicites dans les paramètres du constructeur) | **Partiellement** (visibles via les méthodes setter) | **Non** (dépendances masquées à l'intérieur de la classe) |
| **Classe utilisable avec `new` hors conteneur sans dépendance `null` ?** | **Oui** (le compilateur impose de fournir les dépendances) | **Non garanti** (l'objet peut être instancié sans appeler les setters) | **Non** (les attributs restent `null` sauf si réinjectés par réflexion) |
| **Signalé par SonarQube for IDE ?** | **Non** (recommandé comme bonne pratique) | **Oui** / Déconseillé pour dépendances obligatoires | **Oui** (Règle `java:S6813` : *Use constructor injection instead*) |

---

## Section B. Services

| Service | Dépendances injectées | Mode d'injection | Justification |
| :--- | :--- | :--- | :--- |
| **`ContratServiceImpl`** | `IContratRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, immutabilité, testabilité sans conteneur. |
| **`AgenceServiceImpl`** | `IAgenceRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, respect du principe de couplage faible. |
| **`EmployeServiceImpl`** | `IEmployeRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, instanciation contrôlée. |
| **`VehiculeServiceImpl`** | `IVehiculeRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, sécurité multi-thread. |
| **`EquipementServiceImpl`** | `IEquipementRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, abstraction via interface. |
| **`ClientServiceImpl`** | `IClientRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, intégrité garantie dès l'instanciation. |
| **`ReservationServiceImpl`** | `IReservationRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, respect des standards Spring. |
| **`PaiementServiceImpl`** | `IPaiementRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, service en lecture seule sans état. |
| **`MaintenanceServiceImpl`** | `IMaintenanceRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final`, injection sécurisée et claire. |

---

## Section C. Messages d'erreur du conteneur

### Message 1
```text
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.ContratController
required a bean of type 'tn.esprit.autoloc.service.IContratService' that could not be found.
```
- **Exception sous-jacente** : `NoSuchBeanDefinitionException` (encapsulée dans `UnsatisfiedDependencyException`).
- **Deux causes les plus probables côté `ContratServiceImpl`** :
  1. *Oubli de l'annotation de stéréotype* : la classe `ContratServiceImpl` n'a pas été annotée avec `@Service` (ou `@Component`), donc Spring ne l'a pas enregistrée comme bean.
  2. *Package hors du scan des composants* : la classe `ContratServiceImpl` se trouve dans un package non scanné par `@SpringBootApplication` (placée en dehors de l'arborescence du package racine).
- **Correction** : Ajouter l'annotation `@Service` sur `ContratServiceImpl` et s'assurer que son package se trouve sous le package racine scanné par Spring Boot.

### Message 2
```text
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.NotificationController
required a single bean, but 2 were found:
- emailNotificateur
- smsNotificateur
```
- **Exception sous-jacente** : `NoUniqueBeanDefinitionException`.
- **Cause** : Deux implémentations distinctes de l'interface `INotificateur` existent dans le conteneur (`emailNotificateur` et `smsNotificateur`), et Spring ne sait pas laquelle injecter.
- **Deux corrections possibles** :
  1. *Option 1 : Utilisation de `@Primary`*
     - **Emplacement** : Sur la classe d'implémentation par défaut.
     - **Exemple** :
       ```java
       @Component
       @Primary
       public class EmailNotificateur implements INotificateur { ... }
       ```
  2. *Option 2 : Utilisation de `@Qualifier`*
     - **Emplacement** : Sur le paramètre du constructeur au point d'injection dans `NotificationController`.
     - **Exemple** :
       ```java
       public NotificationController(@Qualifier("emailNotificateur") INotificateur notificateur) {
           this.notificateur = notificateur;
       }
       ```

### Message 3
```text
The dependencies of some of the beans in the application context form a cycle:
clientServiceImpl -> reservationServiceImpl -> clientServiceImpl
```
- **Pourquoi l'injection par constructeur rend-elle ce cycle impossible à contourner ?**
  Pour créer l'instance de `ClientServiceImpl`, Spring doit d'abord résoudre et fournir ses paramètres de constructeur, ce qui nécessite de créer `ReservationServiceImpl`. Mais pour créer `ReservationServiceImpl`, Spring doit d'abord créer `ClientServiceImpl`. Aucun des deux objets ne peut être instancié en premier. Contrairement à l'injection par setter ou champ où des instances partiellement construites peuvent être manipulées, le constructeur impose la disponibilité immédiate des dépendances.
- **Piste de conception pour supprimer ce cycle** :
  Extraire la logique croisée dans un nouveau service dédié (ex: `ReservationWorkflowService` ou `ClientReservationFacade`), ou utiliser un bus d'événements asynchrones (`ApplicationEventPublisher` / `@EventListener`) pour découpler la notification d'événements entre les deux domaines métier.

---

## Section D. Anomalies SonarQube for IDE

1. **Règle `java:S6813` — *Use constructor injection instead of field injection*** :
   - *Explication* : L'injection par attribut (`@Autowired` sur champ privé) masque les dépendances de la classe, empêche l'utilisation de champs `final` (mutabilité indésirable) et rend la classe inutilisable hors d'un conteneur IoC (les tests unitaires purs échouent avec des `NullPointerException`).
   - *Correction apportée* : Déclaration des dépendances en `private final` et injection via constructeur généré automatiquement par `@RequiredArgsConstructor` de Lombok.
2. **Règle `java:S112` — *Generic exceptions should never be thrown*** :
   - *Explication* : Lever des exceptions génériques telles que `RuntimeException` ou `Exception` ne fournit aucune sémantique métier à l'appelant et complique la gestion des erreurs ciblées.
   - *Correction apportée* : Création d'une exception métier explicite `ResourceNotFoundException` étendant `RuntimeException`, utilisée dans les méthodes `findById` et `deleteById`.
3. **Bonne pratique Lombok & Clean Code — *Éviter `@Data` sur les entités JPA*** :
   - *Explication* : `@Data` génère automatiquement `equals`, `hashCode` et `toString` basés sur tous les champs. Sur des entités JPA comportant des relations bidirectionnelles (`@OneToMany`, `@ManyToOne`, `@ManyToMany`), cela déclenche des récursions infinies (`StackOverflowError`) et force le chargement de relations Lazy non souhaitées.
   - *Correction apportée* : Utilisation ciblée de `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` et `@ToString(exclude = {...})`.

---

## Section E. Questions de compréhension

1. **Où est le `new` de `ContratServiceImpl` et qui l'exécute ?**
   - Il n'y a aucun `new ContratServiceImpl(...)` écrit dans le code source de l'application. C'est le conteneur Spring IoC (`ApplicationContext` / `BeanFactory`) qui instancie la classe par introspection/réflexion lors de la phase de démarrage et de résolution du graphe de dépendances.
2. **Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?**
   - En vertu du principe d'inversion des dépendances (DIP de SOLID), les modules de haut niveau doivent dépendre d'abstractions et non de détails d'implémentation. Cela garantit un faible couplage, facilite la substitution par des mocks lors des tests unitaires et permet d'interchanger l'implémentation métier sans modifier les couches clientes.
3. **Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.**
   - Un bean en scope singleton n'existe qu'en une seule instance partagée entre tous les threads de l'application (chaque requête HTTP est traitée par un thread distinct). Si le service conserve un état mutable (ex: `private Contrat contratCourant;`), une requête A peut modifier ce champ pendant qu'une requête B l'utilise, provoquant une condition de course (*race condition*), des corruptions de données ou des fuites d'informations entre clients.
4. **Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` au lieu de lui passer directement l'objet reçu ?**
   - Passer directement l'objet reçu à `save()` écraserait toutes les propriétés non transmises avec des valeurs `null` et risquerait de rompre les relations d'association (par exemple avec `orphanRemoval=true`, la collection des paiements pourrait être vidée). En chargeant d'abord l'entité via `findById()`, on vérifie son existence en base (ou on lève `ResourceNotFoundException`), on maintient l'entité dans l'état *managed* d'Hibernate et on ne recopie que les champs modifiables autorisés.
5. **Quelle différence entre `@Component` et `@Bean` ? Entre `@Primary` et `@Qualifier` ? Pourquoi `IPaiementService` n'expose-t-il ni création, ni modification, ni suppression ?**
   - `@Component` vs `@Bean` : `@Component` est une annotation de classe détectée automatiquement par le scan de composants. `@Bean` est une annotation de méthode dans une classe `@Configuration` permettant d'enregistrer explicitement un bean (indispensable pour les classes tierces ou du JDK dont on ne possède pas le code source).
   - `@Primary` vs `@Qualifier` : `@Primary` indique qu'un bean est prioritaire par défaut lorsque plusieurs candidats du même type existent. `@Qualifier("nom")` s'utilise au point d'injection pour sélectionner explicitement le bean désiré.
   - `IPaiementService` en lecture seule : Un paiement est un composant financier dont la création ou modification doit impérativement être orchestrée par le `Contrat` associé pour garantir l'intégrité transactionnelle et la cohérence du montant total.
