package tn.esprit.tpautoloc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.service.factory.NotificationFactory;
import tn.esprit.tpautoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.tpautoloc.service.observer.EmailNotificationObserver;
import tn.esprit.tpautoloc.service.observer.IReservationObserver;
import tn.esprit.tpautoloc.service.observer.ReservationSubject;
import tn.esprit.tpautoloc.service.observer.SmsNotificationObserver;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationSubject reservationSubject;

    @Autowired
    private TarificationStrategyFactory tarificationStrategyFactory;

    @Autowired
    private NotificationFactory notificationFactory;

    @Test
    @DisplayName("Test création de réservation avec notification Observer (EMAIL + SMS)")
    void testCreerReservation() {
        Client client = Client.builder()
                .nom("Ben Salah")
                .prenom("Ahmed")
                .email("ahmed.bensalah@email.com")
                .telephone("+216 20 123 456")
                .build();

        Reservation reservation = reservationService.creerReservation(client);

        assertNotNull(reservation);
        assertEquals(client, reservation.getClient());
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
        assertNotNull(reservation.getDateDebut());
    }

    @Test
    @DisplayName("Test annulation de réservation avec notification Observer (EMAIL + SMS)")
    void testAnnulerReservation() {
        Client client = Client.builder()
                .nom("Trabelsi")
                .prenom("Sarra")
                .email("sarra.trabelsi@email.com")
                .telephone("+216 55 987 654")
                .build();

        Reservation reservation = Reservation.builder()
                .idReservation(42L)
                .client(client)
                .dateDebut(LocalDate.now())
                .statut(StatutReservation.CONFIRMEE)
                .build();

        Reservation annulee = reservationService.annulerReservation(reservation);

        assertNotNull(annulee);
        assertEquals(StatutReservation.ANNULEE, annulee.getStatut());
    }

    @Test
    @DisplayName("Test ReservationSubject maintient et notifie la liste d'observateurs")
    void testReservationSubject() {
        assertNotNull(reservationSubject);
        assertNotNull(reservationSubject.getObservers());
        assertTrue(reservationSubject.getObservers().size() >= 2);
    }

    @Test
    @DisplayName("Test NotificationFactory pour EMAIL et SMS")
    void testNotificationFactory() {
        IReservationObserver emailObserver = notificationFactory.creerObservateur("EMAIL");
        assertNotNull(emailObserver);
        assertInstanceOf(EmailNotificationObserver.class, emailObserver);

        IReservationObserver smsObserver = notificationFactory.creerObservateur("SMS");
        assertNotNull(smsObserver);
        assertInstanceOf(SmsNotificationObserver.class, smsObserver);

        assertThrows(IllegalArgumentException.class, () -> notificationFactory.creerObservateur("INCONNU"));
        assertThrows(IllegalArgumentException.class, () -> notificationFactory.creerObservateur(null));
    }

    @Test
    @DisplayName("Test TarificationStrategyFactory - Résolution dynamique de stratégie")
    void testTarificationStrategyFactory() {
        Vehicule v = new Vehicule();
        v.setTarifJournalier(new BigDecimal("100.00"));

        // 1. Longue durée (>= 7 jours)
        Reservation resLongue = Reservation.builder()
                .dateDebut(LocalDate.of(2026, 11, 2)) // Lundi
                .dateFin(LocalDate.of(2026, 11, 12)) // 10 jours
                .build();
        ITarificationStrategy stratLongue = tarificationStrategyFactory.resoudre(resLongue);
        assertNotNull(stratLongue);
        assertEquals("LONGUE_DUREE", stratLongue.getCode());
        BigDecimal tarifLongue = stratLongue.calculerTarif(v, resLongue);
        assertEquals(0, new BigDecimal("800.00").compareTo(tarifLongue));

        // 2. Week-end (début samedi <= 3 jours)
        Reservation resWeekend = Reservation.builder()
                .dateDebut(LocalDate.of(2026, 11, 7)) // Samedi
                .dateFin(LocalDate.of(2026, 11, 9)) // Lundi (2 jours)
                .build();
        ITarificationStrategy stratWeekend = tarificationStrategyFactory.resoudre(resWeekend);
        assertNotNull(stratWeekend);
        assertEquals("WEEKEND", stratWeekend.getCode());
        BigDecimal tarifWeekend = stratWeekend.calculerTarif(v, resWeekend);
        assertEquals(0, new BigDecimal("180.00").compareTo(tarifWeekend));

        // 3. Standard (3 jours en semaine)
        Reservation resStandard = Reservation.builder()
                .dateDebut(LocalDate.of(2026, 11, 2)) // Lundi
                .dateFin(LocalDate.of(2026, 11, 5)) // Jeudi (3 jours)
                .build();
        ITarificationStrategy stratStandard = tarificationStrategyFactory.resoudre(resStandard);
        assertNotNull(stratStandard);
        assertEquals("STANDARD", stratStandard.getCode());
        BigDecimal tarifStandard = stratStandard.calculerTarif(v, resStandard);
        assertEquals(0, new BigDecimal("300.00").compareTo(tarifStandard));
    }
}
