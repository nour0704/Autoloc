package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Equipement;
import java.util.List;

public interface IEquipementService {
    Equipement create(Equipement equipement);
    Equipement findById(Long id);
    List<Equipement> findAll();
    Equipement update(Long id, Equipement equipement);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
}
