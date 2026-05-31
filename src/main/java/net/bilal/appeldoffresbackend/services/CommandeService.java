package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Commande;
import net.bilal.appeldoffresbackend.repositories.CommandeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id).orElse(null);
    }

    public Commande saveCommande(Commande commande) {
        return commandeRepository.save(commande);
    }

    public Commande updateCommande(Long id,
                                   Commande commande) {

        commande.setId(id);

        return commandeRepository.save(commande);
    }

    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }
}
