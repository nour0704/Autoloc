package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Reservation;
import java.util.List;

public interface IReservationService {
    Reservation create(Reservation reservation);
    Reservation findById(Long id);
    List<Reservation> findAll();
    Reservation update(Long id, Reservation reservation);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
}
