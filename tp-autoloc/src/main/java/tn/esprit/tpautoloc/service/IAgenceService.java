package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Agence;
import java.util.List;

public interface IAgenceService {
    Agence create(Agence agence);
    Agence findById(Long id);
    List<Agence> findAll();
    Agence update(Long id, Agence agence);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
}
