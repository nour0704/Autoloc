package tn.esprit.tpautoloc.service;

import tn.esprit.tpautoloc.domain.Client;
import java.util.List;

public interface IClientService {
    Client create(Client client);
    Client findById(Long id);
    List<Client> findAll();
    Client update(Long id, Client client);
    void deleteById(Long id);
    long count();
    boolean existsById(Long id);
}
