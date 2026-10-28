package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Employe;
import java.util.List;

public interface IEmployeService {
    Employe create(Employe employe);
    Employe findById(Long id);
    List<Employe> findAll();
    Employe update(Long id, Employe employe);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
}
