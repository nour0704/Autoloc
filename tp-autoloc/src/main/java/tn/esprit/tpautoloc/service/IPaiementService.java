package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Paiement;
import java.util.List;

public interface IPaiementService {
    Paiement findById(Long id);
    List<Paiement> findAll();
    long count();
    boolean existsById(Long id);
}
