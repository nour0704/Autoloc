package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Vehicule;
import java.util.List;

public interface IVehiculeService {
    Vehicule create(Vehicule vehicule);
    Vehicule findById(Long id);
    List<Vehicule> findAll();
    Vehicule update(Long id, Vehicule vehicule);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
}
