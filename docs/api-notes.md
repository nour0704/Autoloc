# Note d'Atelier 5 : Spring MVC et SpringDoc (API REST, Postman & Swagger)

---

## Section A. Endpoints de l'API REST

Préfixe d'URL défini globalement via `server.servlet.context-path=/api`.

| Contrôleur | Verbe HTTP & Route | Service appelé | Code succès | Codes d'erreur | Description |
| :--- | :--- | :--- | :---: | :---: | :--- |
| **`ContratController`** | `GET /api/contrats` | `contratService.findAll()` | 200 | — | Liste tous les contrats |
| | `GET /api/contrats/{id}` | `contratService.findById(id)` | 200 | 404 | Consulte un contrat par son ID |
| | `POST /api/contrats` | `contratService.create(contrat)` | 201 | 400 | Crée un contrat (avec ou sans paiements) |
| | `PUT /api/contrats/{id}` | `contratService.update(id, contrat)` | 200 | 400, 404 | Modifie les attributs d'un contrat |
| | `DELETE /api/contrats/{id}` | `contratService.deleteById(id)` | 204 | 404 | Supprime un contrat (cascade vers paiements) |
| **`PaiementController`** | `GET /api/paiements` | `paiementService.findAll()` | 200 | — | Liste tous les paiements (lecture seule) |
| | `GET /api/paiements/{id}` | `paiementService.findById(id)` | 200 | 404 | Consulte un paiement |
| | `POST /api/paiements` | *(non exposé)* | — | 405 | Refusé : création gérée par Contrat |
| **`AgenceController`** | `GET /api/agences` | `agenceService.findAll()` | 200 | — | Liste toutes les agences |
| | `GET /api/agences/{id}` | `agenceService.findById(id)` | 200 | 404 | Consulte une agence |
| | `POST /api/agences` | `agenceService.create(agence)` | 201 | 400 | Crée une agence (nom obligatoire) |
| | `PUT /api/agences/{id}` | `agenceService.update(id, agence)` | 200 | 400, 404 | Modifie une agence |
| | `DELETE /api/agences/{id}` | `agenceService.deleteById(id)` | 204 | 404 | Supprime une agence |
| **`EmployeController`** | `GET /api/employes` | `employeService.findAll()` | 200 | — | Liste tous les employés |
| | `GET /api/employes/{id}` | `employeService.findById(id)` | 200 | 404 | Consulte un employé |
| | `POST /api/employes` | `employeService.create(employe)` | 201 | 400 | Crée un employé (nom, rôle obligatoires) |
| | `PUT /api/employes/{id}` | `employeService.update(id, employe)` | 200 | 400, 404 | Modifie un employé |
| | `DELETE /api/employes/{id}` | `employeService.deleteById(id)` | 204 | 404 | Supprime un employé |
| **`VehiculeController`** | `GET /api/vehicules` | `vehiculeService.findAll()` | 200 | — | Liste tous les véhicules |
| | `GET /api/vehicules/{id}` | `vehiculeService.findById(id)` | 200 | 404 | Consulte un véhicule |
| | `POST /api/vehicules` | `vehiculeService.create(vehicule)` | 201 | 400 | Crée un véhicule (tarif non négatif) |
| | `PUT /api/vehicules/{id}` | `vehiculeService.update(id, vehicule)` | 200 | 400, 404 | Modifie un véhicule |
| | `DELETE /api/vehicules/{id}` | `vehiculeService.deleteById(id)` | 204 | 404 | Supprime un véhicule |
| **`EquipementController`** | `GET /api/equipements` | `equipementService.findAll()` | 200 | — | Liste tous les équipements |
| | `GET /api/equipements/{id}` | `equipementService.findById(id)` | 200 | 404 | Consulte un équipement |
| | `POST /api/equipements` | `equipementService.create(equipement)` | 201 | 400 | Crée un équipement (libellé obligatoire) |
| | `PUT /api/equipements/{id}` | `equipementService.update(id, equipement)` | 200 | 400, 404 | Modifie un équipement |
| | `DELETE /api/equipements/{id}` | `equipementService.deleteById(id)` | 204 | 404 | Supprime un équipement |
| **`ClientController`** | `GET /api/clients` | `clientService.findAll()` | 200 | — | Liste tous les clients |
| | `GET /api/clients/{id}` | `clientService.findById(id)` | 200 | 404 | Consulte un client |
| | `POST /api/clients` | `clientService.create(client)` | 201 | 400 | Crée un client (nom et email obligatoires) |
| | `PUT /api/clients/{id}` | `clientService.update(id, client)` | 200 | 400, 404 | Modifie un client |
| | `DELETE /api/clients/{id}` | `clientService.deleteById(id)` | 204 | 404 | Supprime un client |
| **`ReservationController`**| `GET /api/reservations` | `reservationService.findAll()` | 200 | — | Liste toutes les réservations |
| | `GET /api/reservations/{id}` | `reservationService.findById(id)` | 200 | 404 | Consulte une réservation |
| | `POST /api/reservations` | `reservationService.create(res)` | 201 | 400 | Crée une réservation (dateFin >= dateDebut) |
| | `PUT /api/reservations/{id}` | `reservationService.update(id, res)` | 200 | 400, 404 | Modifie une réservation |
| | `DELETE /api/reservations/{id}` | `reservationService.deleteById(id)` | 204 | 404 | Supprime une réservation |
| **`MaintenanceController`**| `GET /api/maintenances` | `maintenanceService.findAll()` | 200 | — | Liste les opérations de maintenance |
| | `GET /api/maintenances/{id}`| `maintenanceService.findById(id)` | 200 | 404 | Consulte une opération de maintenance |
| | `POST /api/maintenances` | `maintenanceService.create(maint)` | 201 | 400 | Crée une maintenance (dateFin >= dateDebut) |
| | `PUT /api/maintenances/{id}`| `maintenanceService.update(id, maint)` | 200 | 400, 404 | Modifie une maintenance |
| | `DELETE /api/maintenances/{id}`| `maintenanceService.deleteById(id)` | 204 | 404 | Supprime une maintenance |

---

## Section B. Annotations Jackson et Sérialisation JSON

| Association | Annotation retenue | Justification |
| :--- | :--- | :--- |
| **`Contrat.paiements` $\leftrightarrow$ `Paiement.contrat`** | `@JsonManagedReference("contrat-paiements")` sur la collection `Contrat.paiements` et `@JsonBackReference("contrat-paiements")` sur `Paiement.contrat`. | Vraie relation de composition : un contrat porte ses paiements. La partie "Parent" est sérialisée dans le JSON du contrat, tandis que la référence inverse dans l'enfant est masquée pour casser la boucle infinie. |
| **`Agence.vehicules` & `Agence.employes`** | `@JsonIgnore` sur les collections de l'Agence. | Entités indépendantes créées de manière autonome. `@JsonIgnore` évite de charger et de sérialiser l'arbre complet des véhicules et employés lors de la consultation d'une agence. |
| **`Client.reservations`** | `@JsonIgnore` sur la collection de réservations. | Évite la récursion cyclique sans bloquer la désérialisation du client lors de la création d'une réservation. |
| **`Vehicule.reservations` & `maintenances` & `equipements`** | `@JsonIgnore` sur les collections inverses. | Évite les boucles et les proxies Hibernate non initialisés (`LazyInitializationException`). |
| **`Equipement.vehicules`** | `@JsonIgnore` sur les véhicules liés. | Rompt la relation ManyToMany bidirectionnelle lors de la sérialisation de l'équipement. |

### Différence fondamentale entre `@JsonBackReference` et `@JsonIgnore`
- **`@JsonBackReference`** : Fait partie d'un couple bidirectionnel avec `@JsonManagedReference`. Elle ignore le champ à la **sérialisation** (Java $\to$ JSON), mais restaure automatiquement le lien bidirectionnel parent/enfant à la **désérialisation** (JSON $\to$ Java) lors d'une cascade.
- **`@JsonIgnore`** : Exclut totalement et inconditionnellement le champ de la sérialisation ET de la désérialisation JSON.

---

## Section C. Postman et Swagger (OpenAPI)

| Outil | Rôle principal | Exemple concret dans l'Atelier 5 |
| :--- | :--- | :--- |
| **Postman** | Outil client d'exécution et de validation des requêtes HTTP (tests fonctionnels, rejouabilité, assertions de statut et tests de scénarios séquentiels). | Test du scénario de cascade : création d'un contrat avec deux paiements imbriqués via `POST /api/contrats`, capture dynamique de l'identifiant avec `pm.environment.set("contratId", ...)` et vérification que `DELETE /api/contrats/{{contratId}}` renvoie `204`, puis `404` sur le paiement associé (`cascade REMOVE`). |
| **Swagger UI / SpringDoc** | Documentation vivante, interactive et contractuelle de l'API générée automatiquement à partir du code et des annotations OpenAPI. | Exploration interactive sur `http://localhost:8080/api/swagger-ui.html` : visualisation des schémas JSON, des codes de réponse (`200`, `201`, `204`, `400`, `404`) et exécution immédiate via le bouton *"Try it out"*. |

---

## Section D. Anomalies SonarQube for IDE

1. **Règle `java:S6813` — *Field injection should not be used*** :
   - *Explication* : L'injection par champ via `@Autowired` masque les dépendances de la classe, empêche l'immutabilité (`final`) et complique l'écriture de tests unitaires sans conteneur Spring.
   - *Correction apportée* : Injection exclusive par constructeur généré par Lombok `@RequiredArgsConstructor` sur tous les contrôleurs REST.
2. **Règle `java:S112` — *Generic exceptions should never be thrown*** :
   - *Explication* : L'utilisation de `RuntimeException` générique empêche un traitement d'erreur ciblé par la couche web.
   - *Correction apportée* : Utilisation systématique de `ResourceNotFoundException` et `IllegalArgumentException`, interceptées avec précision par `GlobalExceptionHandler`.
3. **Bonne pratique Clean Code — *Couplage fort Entités / API*** :
   - *Explication* : Exposer directement les entités JPA dans les signatures de contrôleurs expose la structure de la base de données et peut provoquer des erreurs de sérialisation Hibernate.
   - *Correction apportée* : Protection par annotations Jackson ciblées (`@JsonManagedReference`, `@JsonBackReference`, `@JsonIgnore`) et configuration `spring.jackson.serialization.fail-on-empty-beans=false`, en attendant l'introduction formelle des DTO (Chapitre 10).

---

## Section E. Questions de compréhension

1. **Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?**
   - Toutes les requêtes HTTP sont interceptées à l'entrée par la **`DispatcherServlet`** (le *Front Controller* de Spring MVC). Elle consulte ensuite le composant **`HandlerMapping`** (notamment `RequestMappingHandlerMapping`), qui maintient une table de correspondance entre les URLs/verbes HTTP et les méthodes de contrôleurs annotées (`@GetMapping`, `@PostMapping`, etc.).
2. **Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de `ContratServiceImpl` ?**
   - Il ne dépend pas de `ContratRepository` car le contrôleur ne doit pas accéder directement aux données : il doit passer par la couche métier qui applique les règles de gestion et de validation.
   - Il dépend de l'interface `IContratService` (et non de l'implémentation `ContratServiceImpl`) pour respecter le principe d'inversion des dépendances (DIP de SOLID) : cela garantit un faible couplage et permet de substituer facilement l'implémentation (par exemple par un mock lors des tests).
3. **Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?**
   - `POST` renvoie **`201 Created`** pour indiquer que la requête a abouti à la création effective d'une nouvelle ressource, généralement accompagnée de l'entité créée dans le corps.
   - `DELETE` renvoie **`204 No Content`** car la suppression a réussi et il n'y a plus aucun contenu à retourner dans le corps de réponse.
   - Une ressource absente renvoie **`404 Not Found`** car il s'agit d'une erreur côté client (l'URL demandée avec cet ID n'existe pas en base). Le code `500 Internal Server Error` est réservé aux pannes logicielles ou exceptions non gérées du serveur : une absence en base est une situation normale prévue par le domaine métier.
4. **Pourquoi `PaiementController` n'expose-t-il que des GET ? Quelle réponse reçoit un POST `/api/paiements` ?**
   - Parce qu'un paiement n'a pas d'existence commerciale autonome : il est intrinsèquement rattaché à un contrat. La création et la modification d'un paiement impactent directement l'état financier du contrat et doivent donc obligatoirement être pilotées par le `ContratController`.
   - Une requête `POST /api/paiements` renvoie le code **`405 Method Not Allowed`** car aucune méthode `@PostMapping` n'est déclarée sur cette route.
5. **Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?**
   - *Risques* : Fuite d'informations internes de la base de données, vulnérabilités de sur-assignation (*over-posting* / injection de champs sensibles), boucles de sérialisation récursives, et erreurs de proxies LAZY non initialisés.
   - *Différence* : `Contrat` et `Paiement` forment un agrégat de composition où les paiements doivent être sérialisés dans la réponse JSON du contrat (`@JsonManagedReference`). En revanche, `Agence` et `Vehicule` sont des agrégats indépendants : charger tous les véhicules lors de la consultation d'une agence alourdirait inutilement la charge réseau et la mémoire, d'où l'exclusion via `@JsonIgnore`.
