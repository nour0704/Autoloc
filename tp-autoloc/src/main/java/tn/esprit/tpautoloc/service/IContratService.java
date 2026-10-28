package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Contrat;
import java.util.List;

public interface IContratService {
    Contrat create(Contrat contrat);
    Contrat findById(Long id);
    List<Contrat> findAll();
    Contrat update(Long id, Contrat contrat);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
}
