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
import java.util.Map;

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

        if (
                bonLivraison.getMontantLivraison() == null
                        || bonLivraison.getMontantLivraison() <= 0
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant livré doit être supérieur à zéro"
            );
        }

        Double totalLivre =
                bonLivraisonRepository
                        .getTotalLivreByCommandeId(
                                commande.getId()
                        );

        double totalActuel =
                totalLivre != null
                        ? totalLivre
                        : 0.0;

        double nouveauTotal =
                totalActuel
                        + bonLivraison.getMontantLivraison();

        double montantCommande =
                commande.getMontantCommande() != null
                        ? commande.getMontantCommande()
                        : 0.0;

        if (nouveauTotal > montantCommande) {

            double montantRestant =
                    montantCommande - totalActuel;

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant total livré dépasse le montant de la commande. "
                            + "Montant restant à livrer : "
                            + montantRestant
                            + " DH"
            );
        }

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

        if (
                bonLivraison.getMontantLivraison() == null
                        || bonLivraison.getMontantLivraison() <= 0
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant livré doit être supérieur à zéro"
            );
        }

        if (
                bonLivraison.getCommande() == null
                        || bonLivraison.getCommande().getId() == null
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Veuillez sélectionner une commande"
            );
        }

        Long nouvelleCommandeId =
                bonLivraison.getCommande().getId();

        Commande nouvelleCommande =
                commandeRepository
                        .findById(nouvelleCommandeId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Commande introuvable"
                                )
                        );

        Double totalLivre =
                bonLivraisonRepository
                        .getTotalLivreByCommandeId(
                                nouvelleCommandeId
                        );

        double totalActuel =
                totalLivre != null
                        ? totalLivre
                        : 0.0;

        boolean memeCommande =
                bonExistant.getCommande() != null
                        && bonExistant
                        .getCommande()
                        .getId()
                        .equals(nouvelleCommandeId);

        double ancienMontant =
                bonExistant.getMontantLivraison() != null
                        ? bonExistant.getMontantLivraison()
                        : 0.0;

        double totalSansBonActuel =
                memeCommande
                        ? totalActuel - ancienMontant
                        : totalActuel;

        double nouveauTotal =
                totalSansBonActuel
                        + bonLivraison.getMontantLivraison();

        double montantCommande =
                nouvelleCommande.getMontantCommande() != null
                        ? nouvelleCommande.getMontantCommande()
                        : 0.0;

        if (nouveauTotal > montantCommande) {

            double montantRestant =
                    montantCommande - totalSansBonActuel;

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant total livré dépasse le montant de la commande. "
                            + "Montant disponible pour ce bon : "
                            + montantRestant
                            + " DH"
            );
        }

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

        bonExistant.setMontantLivraison(
                bonLivraison.getMontantLivraison()
        );

        bonExistant.setCommande(nouvelleCommande);

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

    public Map<String, Double> getResumeCommande(
            Long commandeId
    ) {

        Commande commande =
                commandeRepository
                        .findById(commandeId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Commande introuvable"
                                )
                        );

        Double totalLivre =
                bonLivraisonRepository
                        .getTotalLivreByCommandeId(
                                commandeId
                        );

        double montantCommande =
                commande.getMontantCommande() != null
                        ? commande.getMontantCommande()
                        : 0.0;

        double montantLivre =
                totalLivre != null
                        ? totalLivre
                        : 0.0;

        double montantRestant =
                Math.max(
                        montantCommande - montantLivre,
                        0.0
                );

        return Map.of(
                "montantCommande", montantCommande,
                "montantLivre", montantLivre,
                "montantRestant", montantRestant
        );
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