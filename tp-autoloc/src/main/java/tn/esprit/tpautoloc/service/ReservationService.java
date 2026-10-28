package tn.esprit.tpautoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.service.observer.ReservationSubject;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationSubject reservationSubject;

    /**
     * Crée une réservation et notifie tous les observateurs
     */
    public Reservation creerReservation(Client client) {
        Reservation reservation = Reservation.builder()
                .client(client)
                .dateDebut(LocalDate.now())
                .statut(StatutReservation.CONFIRMEE.name())
                .build();

        // Déclenche le process métier via le pattern Observer
        reservationSubject.notifierConfirmation(reservation);

        return reservation;
    }

    /**
     * Annule une réservation existante et notifie tous les observateurs
     */
    public Reservation annulerReservation(Reservation reservation) {
        reservation.setStatut(StatutReservation.ANNULEE.name());
        String email = (reservation.getClient() != null) ? reservation.getClient().getEmail() : "inconnu";
        log.info("Reservation #{} annulée pour le client {}",
                reservation.getId(), email);

        reservationSubject.notifierAnnulation(reservation);

        return reservation;
    }
}
