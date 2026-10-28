package tn.esprit.tpautoloc.service.observer;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sujet observable (GoF Observer) : maintient la liste des observateurs
 * et les notifie lors des changements d'etat d'une reservation.
 * Tous les beans IReservationObserver de Spring sont injectes automatiquement.
 */
@Component
public class ReservationSubject {

    private final List<IReservationObserver> observers;

    public ReservationSubject(List<IReservationObserver> observers) {
        this.observers = new ArrayList<>(observers);
    }

    public void notifierConfirmation(Reservation reservation) {
        observers.forEach(o -> o.onReservationConfirmee(reservation));
    }

    public void notifierAnnulation(Reservation reservation) {
        observers.forEach(o -> o.onReservationAnnulee(reservation));
    }

    public void ajouterObservateur(IReservationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void retirerObservateur(IReservationObserver observer) {
        observers.remove(observer);
    }

    public List<IReservationObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }
}
