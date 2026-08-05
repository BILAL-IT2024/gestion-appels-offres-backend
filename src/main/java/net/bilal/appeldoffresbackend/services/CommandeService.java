package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Commande;
import net.bilal.appeldoffresbackend.repositories.CommandeRepository;
import org.springframework.stereotype.Service;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.repositories.MarcheRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final MarcheRepository marcheRepository;

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id).orElse(null);
    }

    public Commande saveCommande(Commande commande) {

        if (
                commande.getMarche() == null
                        || commande.getMarche().getId() == null
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Veuillez sélectionner un marché"
            );
        }

        if (
                commande.getMontantCommande() == null
                        || commande.getMontantCommande() <= 0
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant de la commande doit être supérieur à zéro"
            );
        }

        Long marcheId =
                commande.getMarche().getId();

        Marche marche = marcheRepository
                .findById(marcheId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Marché introuvable"
                        )
                );

        if (marche.getMontantMarche() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le marché sélectionné ne possède pas de montant"
            );
        }

        Double totalCommandes =
                commandeRepository
                        .getTotalCommandesByMarcheId(
                                marche.getId()
                        );

        double totalActuel =
                totalCommandes != null
                        ? totalCommandes
                        : 0.0;

        double montantMarche =
                marche.getMontantMarche();

        double nouveauTotal =
                totalActuel
                        + commande.getMontantCommande();

        if (nouveauTotal > montantMarche) {

            double montantRestant =
                    montantMarche - totalActuel;

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant total des commandes dépasse "
                            + "le montant du marché. "
                            + "Montant restant disponible : "
                            + montantRestant
                            + " DH"
            );
        }

        commande.setMarche(marche);

        return commandeRepository.save(commande);
    }

    public Commande updateCommande(Long id,
                                   Commande commande) {

        commande.setId(id);

        return commandeRepository.save(commande);
    }

    public List<Commande> rechercherCommandes(String keyword) {
        return commandeRepository.findByNumeroCommandeContainingIgnoreCase(keyword);
    }

    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }
}
