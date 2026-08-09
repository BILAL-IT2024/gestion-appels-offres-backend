package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.BonLivraison;
import net.bilal.appeldoffresbackend.entities.Commande;
import net.bilal.appeldoffresbackend.repositories.BonLivraisonRepository;
import net.bilal.appeldoffresbackend.repositories.CommandeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonLivraisonService {

    private final BonLivraisonRepository bonLivraisonRepository;
    private final CommandeRepository commandeRepository;


    public List<BonLivraison> getAllBonsLivraison() {
        return bonLivraisonRepository.findAll();
    }


    public BonLivraison getBonLivraisonById(Long id) {

        return bonLivraisonRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Bon de livraison introuvable"
                        )
                );
    }


    public BonLivraison saveBonLivraison(
            BonLivraison bonLivraison
    ) {

        if (
                bonLivraison.getCommande() == null
                        || bonLivraison.getCommande().getId() == null
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Veuillez sélectionner une commande"
            );
        }

        Commande commande =
                commandeRepository
                        .findById(
                                bonLivraison
                                        .getCommande()
                                        .getId()
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Commande introuvable"
                                )
                        );

        bonLivraison.setCommande(commande);

        return bonLivraisonRepository.save(
                bonLivraison
        );
    }


    public BonLivraison updateBonLivraison(
            Long id,
            BonLivraison bonLivraison
    ) {

        BonLivraison bonExistant =
                getBonLivraisonById(id);

        bonExistant.setNumeroBon(
                bonLivraison.getNumeroBon()
        );

        bonExistant.setDateLivraison(
                bonLivraison.getDateLivraison()
        );

        bonExistant.setObjet(
                bonLivraison.getObjet()
        );

        bonExistant.setStatut(
                bonLivraison.getStatut()
        );

        if (
                bonLivraison.getCommande() != null
                        && bonLivraison
                        .getCommande()
                        .getId() != null
        ) {

            Commande commande =
                    commandeRepository
                            .findById(
                                    bonLivraison
                                            .getCommande()
                                            .getId()
                            )
                            .orElseThrow(() ->
                                    new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                                            "Commande introuvable"
                                    )
                            );

            bonExistant.setCommande(commande);
        }

        return bonLivraisonRepository.save(
                bonExistant
        );
    }


    public List<BonLivraison> rechercherBonsLivraison(
            String keyword
    ) {

        return bonLivraisonRepository
                .findByNumeroBonContainingIgnoreCase(
                        keyword
                );
    }


    public List<BonLivraison> getBonsByCommande(
            Long commandeId
    ) {

        return bonLivraisonRepository
                .findByCommandeId(commandeId);
    }


    public void deleteBonLivraison(Long id) {

        if (!bonLivraisonRepository.existsById(id)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Bon de livraison introuvable"
            );
        }

        bonLivraisonRepository.deleteById(id);
    }
}