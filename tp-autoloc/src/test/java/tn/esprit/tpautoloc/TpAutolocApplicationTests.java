package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.domain.enums.*;
import tn.esprit.tpautoloc.repository.*;
import tn.esprit.tpautoloc.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class TpAutolocApplicationTests {

    // ========================================================
    // INJECTION DES SERVICES (ATELIER 4)
    // ========================================================
    @Autowired
    private IAgenceService agenceService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IEquipementService equipementService;

    @Autowired
    private IContratService contratService;

    @Autowired
    private IMaintenanceService maintenanceService;

    @Autowired
    private IReservationService reservationService;

    @Autowired
    private IPaiementService paiementService;

    // ========================================================
    // INJECTION DES REPOSITORIES (ATELIER 3)
    // ========================================================
    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IContratRepository contratRepository;

    @Autowired
    private IEmployeRepository employeRepository;

    @Autowired
    private IPaiementRepository paiementRepository;

    @Autowired
    private IEquipementRepository equipementRepository;

    @Autowired
    private IMaintenanceRepository maintenanceRepository;

    @Autowired
    private IReservationRepository reservationRepository;

    // ========================================================
    // ATELIER 4 - PARTIE 2 : TEST GUIDE DE AGENCE SERVICE
    // ========================================================
    @Test
    void testAgenceService() {
        System.out.println("========== ATELIER 4 - PARTIE 2 : TEST AGENCE SERVICE ==========");

        // 1. Créer une instance d'Agence avec les attributs disponibles
        Agence agence = new Agence();
        agence.setNom("AutoLoc Ennasr");
        agence.setVille("Ariana");
        agence.setAdresse("Avenue Hedi Nouira");
        agence.setTelephone("70123456");

        // 2. Enregistrer l'agence avec create()
        Agence savedAgence = agenceService.create(agence);
        assertNotNull(savedAgence, "L'agence créée ne doit pas être null");
        assertNotNull(savedAgence.getIdAgence(), "L'id de l'agence doit être généré");

        // 3. Afficher l'objet retourné
        System.out.println("[create] Agence créée : " + savedAgence);

        // 4. Afficher toutes les agences avec findAll()
        List<Agence> agences = agenceService.findAll();
        assertNotNull(agences);
        assertFalse(agences.isEmpty(), "La liste des agences ne doit pas être vide");
        System.out.println("[findAll] Liste des agences : " + agences);

        // 5. Récupérer une agence avec findById() et afficher le résultat
        Agence foundAgence = agenceService.findById(savedAgence.getIdAgence());
        assertNotNull(foundAgence);
        assertEquals("AutoLoc Ennasr", foundAgence.getNom());
        System.out.println("[findById] Agence trouvée : " + foundAgence);

        // Test update()
        savedAgence.setAdresse("Avenue Hedi Nouira Résidence Les Pins");
        Agence updatedAgence = agenceService.update(savedAgence.getIdAgence(), savedAgence);
        assertEquals("Avenue Hedi Nouira Résidence Les Pins", updatedAgence.getAdresse());
        System.out.println("[update] Agence modifiée : " + updatedAgence);

        // 6. Afficher le nombre d'agences avec count()
        long count = agenceService.count();
        assertTrue(count > 0);
        System.out.println("[count] Nombre total d'agences : " + count);

        // 7. Supprimer l'agence avec deleteById(), puis vérifier qu'elle n'existe plus
        Long idToDelete = savedAgence.getIdAgence();
        agenceService.deleteById(idToDelete);
        boolean existsAfterDelete = agenceService.existsById(idToDelete);
        assertFalse(existsAfterDelete, "L'agence ne doit plus exister après deleteById()");
        System.out.println("[deleteById] Agence supprimée. Existe encore ? " + existsAfterDelete);
        System.out.println("================================================================\n");
    }

    // ========================================================
    // ATELIER 4 - PARTIE 3 : TEST GUIDE DE CLIENT SERVICE
    // ========================================================
    @Test
    void testClientService() {
        System.out.println("========== ATELIER 4 - PARTIE 3 : TEST CLIENT SERVICE ==========");

        // 1. Créer deux clients en utilisant les attributs réellement présents dans l'entité Client
        Client client1 = new Client();
        client1.setNom("Ayari");
        client1.setPrenom("Yassine");
        client1.setEmail("yassine.ayari@esprit.tn");
        client1.setTelephone("50112233");
        client1.setNumPermis("PERMIS-001");
        client1.setDateInscription(LocalDate.now());

        Client client2 = new Client();
        client2.setNom("Kacem");
        client2.setPrenom("Nour");
        client2.setEmail("nour.kacem@esprit.tn");
        client2.setTelephone("55443322");
        client2.setNumPermis("PERMIS-002");
        client2.setDateInscription(LocalDate.now());

        // 2. Enregistrer les deux clients
        Client saved1 = clientService.create(client1);
        Client saved2 = clientService.create(client2);
        assertNotNull(saved1);
        assertNotNull(saved1.getIdClient());
        assertNotNull(saved2);
        assertNotNull(saved2.getIdClient());
        System.out.println("[create] Client 1 créé : " + saved1);
        System.out.println("[create] Client 2 créé : " + saved2);

        // 3. Afficher la liste avec findAll()
        List<Client> clients = clientService.findAll();
        assertNotNull(clients);
        assertTrue(clients.size() >= 2);
        System.out.println("[findAll] Liste des clients : " + clients);

        // 4. Rechercher un client avec findById()
        Client foundClient = clientService.findById(saved1.getIdClient());
        assertNotNull(foundClient);
        assertEquals("Ayari", foundClient.getNom());
        System.out.println("[findById] Client trouvé : " + foundClient);

        // Test update()
        saved1.setTelephone("50999888");
        Client updatedClient = clientService.update(saved1.getIdClient(), saved1);
        assertEquals("50999888", updatedClient.getTelephone());
        System.out.println("[update] Client modifié : " + updatedClient);

        // 5. Afficher le nombre de clients avec count()
        long count = clientService.count();
        assertTrue(count >= 2);
        System.out.println("[count] Nombre total de clients : " + count);

        // 6. Supprimer le client avec deleteById(), puis vérifier qu'il n'existe plus
        Long idToDelete = saved2.getIdClient();
        clientService.deleteById(idToDelete);
        boolean existsAfterDelete = clientService.existsById(idToDelete);
        assertFalse(existsAfterDelete);
        System.out.println("[deleteById] Client supprimé. Existe encore ? " + existsAfterDelete);
        System.out.println("================================================================\n");
    }

    // ========================================================
    // ATELIER 4 - PARTIE 4 : TEST GUIDE DE VEHICULE SERVICE
    // ========================================================
    @Test
    void testVehiculeService() {
        System.out.println("========== ATELIER 4 - PARTIE 4 : TEST VEHICULE SERVICE ==========");

        // 1. Créer un véhicule avec les attributs de l'entité Vehicule
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("240-TN-1122");
        vehicule.setMarque("Volkswagen");
        vehicule.setModele("Golf 8");
        vehicule.setCategorie(CategorieVehicule.BERLINE);
        vehicule.setTarifJournalier(new BigDecimal("150.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        // 2. Enregistrer le véhicule
        Vehicule savedVehicule = vehiculeService.create(vehicule);
        assertNotNull(savedVehicule);
        assertNotNull(savedVehicule.getIdVehicule());
        System.out.println("[create] Véhicule créé : " + savedVehicule);

        // 3. Afficher tous les véhicules avec findAll()
        List<Vehicule> vehicules = vehiculeService.findAll();
        assertNotNull(vehicules);
        assertFalse(vehicules.isEmpty());
        System.out.println("[findAll] Liste des véhicules : " + vehicules);

        // 4. Rechercher une voiture avec findById()
        Vehicule foundVehicule = vehiculeService.findById(savedVehicule.getIdVehicule());
        assertNotNull(foundVehicule);
        assertEquals("Volkswagen", foundVehicule.getMarque());
        System.out.println("[findById] Véhicule trouvé : " + foundVehicule);

        // Test update()
        savedVehicule.setTarifJournalier(new BigDecimal("165.00"));
        savedVehicule.setStatut(StatutVehicule.LOUE);
        Vehicule updatedVehicule = vehiculeService.update(savedVehicule.getIdVehicule(), savedVehicule);
        assertEquals(new BigDecimal("165.00"), updatedVehicule.getTarifJournalier());
        assertEquals(StatutVehicule.LOUE, updatedVehicule.getStatut());
        System.out.println("[update] Véhicule modifié : " + updatedVehicule);

        // 5. Afficher le nombre de véhicules avec count()
        long count = vehiculeService.count();
        assertTrue(count > 0);
        System.out.println("[count] Nombre total de véhicules : " + count);

        // 6. Supprimer le véhicule avec deleteById()
        Long idToDelete = savedVehicule.getIdVehicule();
        vehiculeService.deleteById(idToDelete);
        boolean existsAfterDelete = vehiculeService.existsById(idToDelete);
        assertFalse(existsAfterDelete);
        System.out.println("[deleteById] Véhicule supprimé. Existe encore ? " + existsAfterDelete);
        System.out.println("==================================================================\n");
    }

    // ========================================================
    // ATELIER 4 - PARTIE 5 : EXERCICE AUTONOME
    // ========================================================

    /**
     * Choix 1 : IEmployeService
     */
    @Test
    void testEmployeService() {
        System.out.println("========== ATELIER 4 - PARTIE 5 : TEST EMPLOYE SERVICE ==========");

        // 1. Création avec create()
        Employe employe = new Employe();
        employe.setNom("Mejri");
        employe.setPrenom("Hassen");
        employe.setRole(RoleEmploye.MANAGER);

        Employe savedEmploye = employeService.create(employe);
        assertNotNull(savedEmploye);
        assertNotNull(savedEmploye.getIdEmploye());
        System.out.println("[create] Employé créé : " + savedEmploye);

        // 2. Lecture avec findById() et findAll()
        Employe found = employeService.findById(savedEmploye.getIdEmploye());
        assertNotNull(found);
        System.out.println("[findById] Employé trouvé : " + found);

        List<Employe> employes = employeService.findAll();
        assertFalse(employes.isEmpty());
        System.out.println("[findAll] Liste des employés : " + employes);

        // 3. Vérification avec existsById()
        boolean exists = employeService.existsById(savedEmploye.getIdEmploye());
        assertTrue(exists);
        System.out.println("[existsById] Existe : " + exists);

        // 4. Suppression avec deleteById()
        employeService.deleteById(savedEmploye.getIdEmploye());
        boolean existsAfterDelete = employeService.existsById(savedEmploye.getIdEmploye());
        assertFalse(existsAfterDelete);
        System.out.println("[deleteById] Employé supprimé. Existe encore ? " + existsAfterDelete);
        System.out.println("=================================================================\n");
    }

    /**
     * Choix 2 : IEquipementService
     */
    @Test
    void testEquipementService() {
        System.out.println("========== ATELIER 4 - PARTIE 5 : TEST EQUIPEMENT SERVICE ==========");

        // 1. Création avec create()
        Equipement equipement = new Equipement();
        equipement.setLibelle("Siège Bébé Confort");

        Equipement savedEquipement = equipementService.create(equipement);
        assertNotNull(savedEquipement);
        assertNotNull(savedEquipement.getIdEquipement());
        System.out.println("[create] Equipement créé : " + savedEquipement);

        // 2. Lecture avec findById() et findAll()
        Equipement found = equipementService.findById(savedEquipement.getIdEquipement());
        assertNotNull(found);
        System.out.println("[findById] Equipement trouvé : " + found);

        List<Equipement> equipements = equipementService.findAll();
        assertFalse(equipements.isEmpty());
        System.out.println("[findAll] Liste des équipements : " + equipements);

        // 3. Vérification avec existsById()
        boolean exists = equipementService.existsById(savedEquipement.getIdEquipement());
        assertTrue(exists);
        System.out.println("[existsById] Existe : " + exists);

        // 4. Suppression avec deleteById()
        equipementService.deleteById(savedEquipement.getIdEquipement());
        boolean existsAfterDelete = equipementService.existsById(savedEquipement.getIdEquipement());
        assertFalse(existsAfterDelete);
        System.out.println("[deleteById] Equipement supprimé. Existe encore ? " + existsAfterDelete);
        System.out.println("====================================================================\n");
    }

    /**
     * Choix 3 : IContratService
     */
    @Test
    void testContratService() {
        System.out.println("========== ATELIER 4 - PARTIE 5 : TEST CONTRAT SERVICE ==========");

        // 1. Création avec create()
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("680.00"));
        contrat.setValide(true);

        Contrat savedContrat = contratService.create(contrat);
        assertNotNull(savedContrat);
        assertNotNull(savedContrat.getIdContrat());
        System.out.println("[create] Contrat créé : " + savedContrat);

        // 2. Lecture avec findById() et findAll()
        Contrat found = contratService.findById(savedContrat.getIdContrat());
        assertNotNull(found);
        System.out.println("[findById] Contrat trouvé : " + found);

        List<Contrat> contrats = contratService.findAll();
        assertFalse(contrats.isEmpty());
        System.out.println("[findAll] Liste des contrats : " + contrats);

        // 3. Vérification avec existsById()
        boolean exists = contratService.existsById(savedContrat.getIdContrat());
        assertTrue(exists);
        System.out.println("[existsById] Existe : " + exists);

        // 4. Suppression avec deleteById()
        contratService.deleteById(savedContrat.getIdContrat());
        boolean existsAfterDelete = contratService.existsById(savedContrat.getIdContrat());
        assertFalse(existsAfterDelete);
        System.out.println("[deleteById] Contrat supprimé. Existe encore ? " + existsAfterDelete);
        System.out.println("=================================================================\n");
    }

    /**
     * Complément : Test de IMaintenanceService
     */
    @Test
    void testMaintenanceService() {
        System.out.println("========== TEST MAINTENANCE SERVICE ==========");
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().plusDays(3));
        maintenance.setDescription("Révision freinage et disques");

        Maintenance saved = maintenanceService.create(maintenance);
        assertNotNull(saved);
        assertNotNull(saved.getIdMaintenance());
        System.out.println("[create] Maintenance créée : " + saved);

        Maintenance found = maintenanceService.findById(saved.getIdMaintenance());
        assertNotNull(found);
        System.out.println("[findById] Maintenance trouvée : " + found);

        maintenanceService.deleteById(saved.getIdMaintenance());
        assertFalse(maintenanceService.existsById(saved.getIdMaintenance()));
        System.out.println("[deleteById] Maintenance supprimée.");
        System.out.println("==============================================\n");
    }

    /**
     * Complément : Test de IReservationService
     */
    @Test
    void testReservationService() {
        System.out.println("========== TEST RESERVATION SERVICE ==========");
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(4));
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saved = reservationService.create(reservation);
        assertNotNull(saved);
        assertNotNull(saved.getIdReservation());
        System.out.println("[create] Réservation créée : " + saved);

        Reservation found = reservationService.findById(saved.getIdReservation());
        assertNotNull(found);
        System.out.println("[findById] Réservation trouvée : " + found);

        reservationService.deleteById(saved.getIdReservation());
        assertFalse(reservationService.existsById(saved.getIdReservation()));
        System.out.println("[deleteById] Réservation supprimée.");
        System.out.println("==============================================\n");
    }

    /**
     * Complément : Test de IPaiementService (Lecture seule)
     */
    @Test
    void testPaiementService() {
        System.out.println("========== TEST PAIEMENT SERVICE ==========");
        long count = paiementService.count();
        System.out.println("[count] Nombre de paiements : " + count);

        List<Paiement> list = paiementService.findAll();
        assertNotNull(list);
        System.out.println("[findAll] Paiements existants : " + list.size());
        System.out.println("===========================================\n");
    }

    // ========================================================
    // ATELIER 3 : TESTS DES REPOSITORIES
    // ========================================================
    @Test
    void testAgenceRepository() {
        Agence agence = new Agence();
        agence.setNom("Agence Rep Test");
        agence.setAdresse("Adresse Rep");
        Agence saved = agenceRepository.save(agence);
        assertNotNull(saved.getIdAgence());

        assertTrue(agenceRepository.findById(saved.getIdAgence()).isPresent());
        agenceRepository.deleteById(saved.getIdAgence());
        assertFalse(agenceRepository.existsById(saved.getIdAgence()));
    }

    @Test
    void testClientRepository() {
        Client c = new Client();
        c.setNom("Test Client Rep");
        c.setEmail("rep@esprit.tn");
        Client saved = clientRepository.save(c);
        assertNotNull(saved.getIdClient());
        assertTrue(clientRepository.existsById(saved.getIdClient()));
    }

    @Test
    void testVehiculeRepository() {
        Vehicule v = new Vehicule();
        v.setImmatriculation("REP-001");
        v.setTarifJournalier(new BigDecimal("100.00"));
        Vehicule saved = vehiculeRepository.save(v);
        assertNotNull(saved.getIdVehicule());
        vehiculeRepository.deleteById(saved.getIdVehicule());
        assertFalse(vehiculeRepository.existsById(saved.getIdVehicule()));
    }
}
