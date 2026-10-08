package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.domain.enums.*;
import tn.esprit.tpautoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class TpAutolocApplicationTests {

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

    /**
     * Partie 2 – Test guidé de AgenceRepository
     */
    @Test
    void testAgenceRepository() {
        System.out.println("========== PARTIE 2 : TEST AGENCE REPOSITORY ==========");

        // 1. Créer une instance d'Agence avec les attributs disponibles
        Agence agence = new Agence();
        agence.setNom("AutoLoc Tunis");
        agence.setVille("Tunis");
        agence.setAdresse("Centre Urbain Nord");
        agence.setTelephone("71000111");

        // 2. Enregistrer l'agence avec save()
        Agence savedAgence = agenceRepository.save(agence);
        assertNotNull(savedAgence, "L'agence enregistrée ne doit pas être null");
        assertNotNull(savedAgence.getIdAgence(), "L'id de l'agence doit être généré");

        // 3. Afficher l'objet retourné par save()
        System.out.println("[save] Agence créée : " + savedAgence);

        // 4. Afficher toutes les agences avec findAll()
        List<Agence> agences = agenceRepository.findAll();
        assertNotNull(agences);
        assertFalse(agences.isEmpty(), "La liste des agences ne doit pas être vide");
        System.out.println("[findAll] Liste des agences : " + agences);

        // 5. Récupérer une agence avec findById() et afficher le résultat
        Optional<Agence> optionalAgence = agenceRepository.findById(savedAgence.getIdAgence());
        assertTrue(optionalAgence.isPresent(), "L'agence doit être trouvée par son id");
        Agence foundAgence = optionalAgence.get();
        assertEquals("AutoLoc Tunis", foundAgence.getNom(), "Le nom de l'agence doit correspondre");
        System.out.println("[findById] Agence trouvée : " + foundAgence);

        // 6. Vérifier l'existence de l'agence avec existsById()
        boolean exists = agenceRepository.existsById(savedAgence.getIdAgence());
        assertTrue(exists, "L'agence doit exister en base");
        System.out.println("[existsById] Existe (id=" + savedAgence.getIdAgence() + ") : " + exists);

        // 7. Afficher le nombre d'agences avec count()
        long count = agenceRepository.count();
        assertTrue(count > 0, "Le nombre d'agences doit être supérieur à zéro");
        System.out.println("[count] Nombre d'agences : " + count);

        // 8. Supprimer l'agence avec deleteById(), puis vérifier qu'elle n'existe plus
        agenceRepository.deleteById(savedAgence.getIdAgence());
        boolean existsAfterDelete = agenceRepository.existsById(savedAgence.getIdAgence());
        assertFalse(existsAfterDelete, "L'agence ne doit plus exister après suppression");
        System.out.println("[deleteById] Agence supprimée. Existe encore ? " + existsAfterDelete);
        System.out.println("=======================================================\n");
    }

    /**
     * Partie 3 – Test guidé de ClientRepository
     */
    @Test
    void testClientRepository() {
        System.out.println("========== PARTIE 3 : TEST CLIENT REPOSITORY ==========");

        // 1. Créer deux clients en utilisant les attributs de l'entité Client
        Client client1 = new Client();
        client1.setNom("Ben Ali");
        client1.setPrenom("Mohamed");
        client1.setEmail("mohamed.benali@esprit.tn");
        client1.setTelephone("20111222");
        client1.setNumPermis("TUN-123456");
        client1.setDateInscription(LocalDate.now());

        Client client2 = new Client();
        client2.setNom("Trabelsi");
        client2.setPrenom("Fatma");
        client2.setEmail("fatma.trabelsi@esprit.tn");
        client2.setTelephone("21333444");
        client2.setNumPermis("TUN-654321");
        client2.setDateInscription(LocalDate.now());

        // 2. Enregistrer les deux clients avec save()
        Client saved1 = clientRepository.save(client1);
        Client saved2 = clientRepository.save(client2);
        assertNotNull(saved1);
        assertNotNull(saved1.getIdClient());
        assertNotNull(saved2);
        assertNotNull(saved2.getIdClient());
        System.out.println("[save] Client 1 créé : " + saved1);
        System.out.println("[save] Client 2 créé : " + saved2);

        // 3. Afficher la liste avec findAll()
        List<Client> clients = clientRepository.findAll();
        assertNotNull(clients);
        assertTrue(clients.size() >= 2, "Il doit y avoir au moins deux clients");
        System.out.println("[findAll] Liste des clients : " + clients);

        // 4. Rechercher un client avec findById()
        Optional<Client> foundClientOpt = clientRepository.findById(saved1.getIdClient());
        assertTrue(foundClientOpt.isPresent());
        Client foundClient = foundClientOpt.get();
        assertEquals("Ben Ali", foundClient.getNom());
        System.out.println("[findById] Client trouvé : " + foundClient);

        // 5. Tester existsById() avec un identifiant existant puis inexistant
        boolean existsClient1 = clientRepository.existsById(saved1.getIdClient());
        boolean existsNonExistent = clientRepository.existsById(999999L);
        assertTrue(existsClient1, "Le client 1 doit exister");
        assertFalse(existsNonExistent, "L'id inexistant ne doit pas être trouvé");
        System.out.println("[existsById] Id existant (" + saved1.getIdClient() + ") : " + existsClient1);
        System.out.println("[existsById] Id inexistant (999999) : " + existsNonExistent);

        // 6. Afficher le nombre de clients avec count()
        long count = clientRepository.count();
        assertTrue(count >= 2);
        System.out.println("[count] Nombre total de clients : " + count);
        System.out.println("=======================================================\n");
    }

    /**
     * Partie 4 – Test guidé de VehiculeRepository
     */
    @Test
    void testVehiculeRepository() {
        System.out.println("========== PARTIE 4 : TEST VEHICULE REPOSITORY ==========");

        // 1. Créer un véhicule avec les attributs de l'entité Vehicule
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("230-TN-7890");
        vehicule.setMarque("Peugeot");
        vehicule.setModele("208");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("95.50"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        // 2. L'enregistrer avec save()
        Vehicule savedVehicule = vehiculeRepository.save(vehicule);
        assertNotNull(savedVehicule);
        assertNotNull(savedVehicule.getIdVehicule());
        System.out.println("[save] Véhicule enregistré : " + savedVehicule);

        // 3. Afficher tous les véhicules avec findAll()
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        assertNotNull(vehicules);
        assertFalse(vehicules.isEmpty());
        System.out.println("[findAll] Liste des véhicules : " + vehicules);

        // 4. Afficher le nombre de véhicules avec count()
        long count = vehiculeRepository.count();
        assertTrue(count > 0);
        System.out.println("[count] Nombre total de véhicules : " + count);

        // 5. Modifier une propriété du véhicule puis utiliser save() pour enregistrer la modification
        savedVehicule.setModele("208 GT");
        savedVehicule.setTarifJournalier(new BigDecimal("120.00"));
        Vehicule updatedVehicule = vehiculeRepository.save(savedVehicule);
        assertEquals("208 GT", updatedVehicule.getModele());
        assertEquals(new BigDecimal("120.00"), updatedVehicule.getTarifJournalier());
        System.out.println("[save (update)] Véhicule modifié : " + updatedVehicule);

        // 6. Supprimer le véhicule avec deleteById()
        vehiculeRepository.deleteById(savedVehicule.getIdVehicule());
        boolean existsAfterDelete = vehiculeRepository.existsById(savedVehicule.getIdVehicule());
        assertFalse(existsAfterDelete);
        System.out.println("[deleteById] Véhicule supprimé. Existe encore ? " + existsAfterDelete);
        System.out.println("=========================================================\n");
    }

    /**
     * Partie 5 – Exercice autonome (Choix 1 : IEmployeRepository)
     */
    @Test
    void testEmployeRepository() {
        System.out.println("========== PARTIE 5 : TEST EMPLOYE REPOSITORY ==========");

        // 1. Création avec save()
        Employe employe = new Employe();
        employe.setNom("Gharbi");
        employe.setPrenom("Ahmed");
        employe.setRole(RoleEmploye.AGENT);

        Employe savedEmploye = employeRepository.save(employe);
        assertNotNull(savedEmploye);
        assertNotNull(savedEmploye.getIdEmploye());
        System.out.println("[save] Employé créé : " + savedEmploye);

        // 2. Lecture avec findById() et findAll()
        Optional<Employe> foundOpt = employeRepository.findById(savedEmploye.getIdEmploye());
        assertTrue(foundOpt.isPresent());
        System.out.println("[findById] Employé trouvé : " + foundOpt.get());

        List<Employe> employes = employeRepository.findAll();
        assertFalse(employes.isEmpty());
        System.out.println("[findAll] Liste des employés : " + employes);

        // 3. Vérification avec existsById()
        boolean exists = employeRepository.existsById(savedEmploye.getIdEmploye());
        assertTrue(exists);
        System.out.println("[existsById] Existe : " + exists);

        // 4. Suppression avec deleteById()
        employeRepository.deleteById(savedEmploye.getIdEmploye());
        boolean existsAfterDelete = employeRepository.existsById(savedEmploye.getIdEmploye());
        assertFalse(existsAfterDelete);
        System.out.println("[deleteById] Employé supprimé. Existe encore ? " + existsAfterDelete);
        System.out.println("========================================================\n");
    }

    /**
     * Partie 5 – Exercice autonome (Choix 2 : IEquipementRepository)
     */
    @Test
    void testEquipementRepository() {
        System.out.println("========== PARTIE 5 : TEST EQUIPEMENT REPOSITORY ==========");

        // 1. Création avec save()
        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS Navigation");

        Equipement savedEquipement = equipementRepository.save(equipement);
        assertNotNull(savedEquipement);
        assertNotNull(savedEquipement.getIdEquipement());
        System.out.println("[save] Equipement créé : " + savedEquipement);

        // 2. Lecture avec findById() et findAll()
        Optional<Equipement> foundOpt = equipementRepository.findById(savedEquipement.getIdEquipement());
        assertTrue(foundOpt.isPresent());
        System.out.println("[findById] Equipement trouvé : " + foundOpt.get());

        List<Equipement> equipements = equipementRepository.findAll();
        assertFalse(equipements.isEmpty());
        System.out.println("[findAll] Liste des équipements : " + equipements);

        // 3. Vérification avec existsById()
        boolean exists = equipementRepository.existsById(savedEquipement.getIdEquipement());
        assertTrue(exists);
        System.out.println("[existsById] Existe : " + exists);

        // 4. Suppression avec deleteById()
        equipementRepository.deleteById(savedEquipement.getIdEquipement());
        boolean existsAfterDelete = equipementRepository.existsById(savedEquipement.getIdEquipement());
        assertFalse(existsAfterDelete);
        System.out.println("[deleteById] Equipement supprimé. Existe encore ? " + existsAfterDelete);
        System.out.println("===========================================================\n");
    }

    /**
     * Partie 5 – Exercice autonome (Choix 3 : IContratRepository)
     */
    @Test
    void testContratRepository() {
        System.out.println("========== PARTIE 5 : TEST CONTRAT REPOSITORY ==========");

        // 1. Création avec save()
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("450.00"));
        contrat.setValide(true);

        Contrat savedContrat = contratRepository.save(contrat);
        assertNotNull(savedContrat);
        assertNotNull(savedContrat.getIdContrat());
        System.out.println("[save] Contrat créé : " + savedContrat);

        // 2. Lecture avec findById() et findAll()
        Optional<Contrat> foundOpt = contratRepository.findById(savedContrat.getIdContrat());
        assertTrue(foundOpt.isPresent());
        System.out.println("[findById] Contrat trouvé : " + foundOpt.get());

        List<Contrat> contrats = contratRepository.findAll();
        assertFalse(contrats.isEmpty());
        System.out.println("[findAll] Liste des contrats : " + contrats);

        // 3. Vérification avec existsById()
        boolean exists = contratRepository.existsById(savedContrat.getIdContrat());
        assertTrue(exists);
        System.out.println("[existsById] Existe : " + exists);

        // 4. Suppression avec deleteById()
        contratRepository.deleteById(savedContrat.getIdContrat());
        boolean existsAfterDelete = contratRepository.existsById(savedContrat.getIdContrat());
        assertFalse(existsAfterDelete);
        System.out.println("[deleteById] Contrat supprimé. Existe encore ? " + existsAfterDelete);
        System.out.println("========================================================\n");
    }

    /**
     * Complément : Test de IPaiementRepository
     */
    @Test
    void testPaiementRepository() {
        System.out.println("========== TEST PAIEMENT REPOSITORY ==========");
        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("200.00"));
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(ModePaiement.CARTE);

        Paiement savedPaiement = paiementRepository.save(paiement);
        assertNotNull(savedPaiement);
        assertNotNull(savedPaiement.getIdPaiement());
        System.out.println("[save] Paiement créé : " + savedPaiement);

        Optional<Paiement> foundOpt = paiementRepository.findById(savedPaiement.getIdPaiement());
        assertTrue(foundOpt.isPresent());
        System.out.println("[findById] Paiement trouvé : " + foundOpt.get());

        boolean exists = paiementRepository.existsById(savedPaiement.getIdPaiement());
        assertTrue(exists);

        paiementRepository.deleteById(savedPaiement.getIdPaiement());
        assertFalse(paiementRepository.existsById(savedPaiement.getIdPaiement()));
        System.out.println("[deleteById] Paiement supprimé.");
        System.out.println("==============================================\n");
    }

    /**
     * Complément : Test de IMaintenanceRepository
     */
    @Test
    void testMaintenanceRepository() {
        System.out.println("========== TEST MAINTENANCE REPOSITORY ==========");
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().plusDays(2));
        maintenance.setDescription("Vidange et changement filtres");

        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        assertNotNull(savedMaintenance);
        assertNotNull(savedMaintenance.getIdMaintenance());
        System.out.println("[save] Maintenance créée : " + savedMaintenance);

        Optional<Maintenance> foundOpt = maintenanceRepository.findById(savedMaintenance.getIdMaintenance());
        assertTrue(foundOpt.isPresent());
        System.out.println("[findById] Maintenance trouvée : " + foundOpt.get());

        boolean exists = maintenanceRepository.existsById(savedMaintenance.getIdMaintenance());
        assertTrue(exists);

        maintenanceRepository.deleteById(savedMaintenance.getIdMaintenance());
        assertFalse(maintenanceRepository.existsById(savedMaintenance.getIdMaintenance()));
        System.out.println("[deleteById] Maintenance supprimée.");
        System.out.println("=================================================\n");
    }

    /**
     * Complément : Test de IReservationRepository
     */
    @Test
    void testReservationRepository() {
        System.out.println("========== TEST RESERVATION REPOSITORY ==========");
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(5));
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation savedReservation = reservationRepository.save(reservation);
        assertNotNull(savedReservation);
        assertNotNull(savedReservation.getIdReservation());
        System.out.println("[save] Réservation créée : " + savedReservation);

        Optional<Reservation> foundOpt = reservationRepository.findById(savedReservation.getIdReservation());
        assertTrue(foundOpt.isPresent());
        System.out.println("[findById] Réservation trouvée : " + foundOpt.get());

        boolean exists = reservationRepository.existsById(savedReservation.getIdReservation());
        assertTrue(exists);

        reservationRepository.deleteById(savedReservation.getIdReservation());
        assertFalse(reservationRepository.existsById(savedReservation.getIdReservation()));
        System.out.println("[deleteById] Réservation supprimée.");
        System.out.println("=================================================\n");
    }
}
