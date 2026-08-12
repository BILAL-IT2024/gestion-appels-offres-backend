package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Facture;
import net.bilal.appeldoffresbackend.entities.Paiement;
import net.bilal.appeldoffresbackend.repositories.FactureRepository;
import net.bilal.appeldoffresbackend.repositories.PaiementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final FactureRepository factureRepository;


    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }


    public Paiement getPaiementById(Long id) {

        return paiementRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Paiement introuvable"
                        )
                );
    }


    public Paiement savePaiement(Paiement paiement) {

        verifierPaiement(paiement, null);

        Paiement paiementEnregistre =
                paiementRepository.save(paiement);

        mettreAJourStatutFacture(
                paiementEnregistre
                        .getFacture()
                        .getId()
        );

        return paiementEnregistre;
    }


    public Paiement updatePaiement(
            Long id,
            Paiement paiement
    ) {

        Paiement ancienPaiement =
                getPaiementById(id);

        Long ancienneFactureId =
                ancienPaiement.getFacture() != null
                        ? ancienPaiement
                        .getFacture()
                        .getId()
                        : null;

        verifierPaiement(
                paiement,
                ancienPaiement
        );

        paiement.setId(id);

        Paiement paiementModifie =
                paiementRepository.save(paiement);

        Long nouvelleFactureId =
                paiementModifie
                        .getFacture()
                        .getId();

        mettreAJourStatutFacture(
                nouvelleFactureId
        );

        if (
                ancienneFactureId != null
                        && !ancienneFactureId
                        .equals(nouvelleFactureId)
        ) {

            mettreAJourStatutFacture(
                    ancienneFactureId
            );
        }

        return paiementModifie;
    }


    public void deletePaiement(Long id) {

        Paiement paiement =
                getPaiementById(id);

        Long factureId =
                paiement.getFacture() != null
                        ? paiement
                        .getFacture()
                        .getId()
                        : null;

        paiementRepository.deleteById(id);

        if (factureId != null) {

            mettreAJourStatutFacture(
                    factureId
            );
        }
    }


    public List<Paiement> rechercherPaiements(
            String keyword
    ) {

        return paiementRepository
                .findByReferencePaiementContainingIgnoreCase(
                        keyword
                );
    }


    public List<Paiement> getPaiementsByFacture(
            Long factureId
    ) {

        return paiementRepository
                .findByFactureId(factureId);
    }


    public Map<String, Double> getResumeFacture(
            Long factureId
    ) {

        Facture facture =
                factureRepository
                        .findById(factureId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Facture introuvable"
                                )
                        );

        double montantTTC =
                facture.getMontantTTC() != null
                        ? facture.getMontantTTC()
                        : 0.0;

        Double totalPaiements =
                paiementRepository
                        .getTotalPaiementsByFactureId(
                                factureId
                        );

        double montantPaye =
                totalPaiements != null
                        ? totalPaiements
                        : 0.0;

        double resteAPayer =
                Math.max(
                        montantTTC - montantPaye,
                        0.0
                );

        return Map.of(
                "montantTTC", montantTTC,
                "montantPaye", montantPaye,
                "resteAPayer", resteAPayer
        );
    }


    private void verifierPaiement(
            Paiement paiement,
            Paiement ancienPaiement
    ) {

        if (
                paiement.getFacture() == null
                        || paiement.getFacture().getId() == null
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Veuillez sélectionner une facture"
            );
        }

        if (
                paiement.getMontantPaiement() == null
                        || paiement.getMontantPaiement() <= 0
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant du paiement doit être supérieur à zéro"
            );
        }

        Long factureId =
                paiement.getFacture().getId();

        Facture facture =
                factureRepository
                        .findById(factureId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Facture introuvable"
                                )
                        );

        if (facture.getMontantTTC() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La facture ne possède pas de montant TTC"
            );
        }

        Double totalPaiements =
                paiementRepository
                        .getTotalPaiementsByFactureId(
                                factureId
                        );

        double totalActuel =
                totalPaiements != null
                        ? totalPaiements
                        : 0.0;

        /*
         * En modification, si on garde la même facture,
         * il faut retirer l'ancien montant avant de tester
         * le nouveau montant.
         */
        if (
                ancienPaiement != null
                        && ancienPaiement.getFacture() != null
                        && ancienPaiement.getFacture().getId() != null
                        && ancienPaiement
                        .getFacture()
                        .getId()
                        .equals(factureId)
        ) {

            totalActuel -=
                    ancienPaiement.getMontantPaiement();
        }

        double nouveauTotal =
                totalActuel
                        + paiement.getMontantPaiement();

        double montantTTC =
                facture.getMontantTTC();

        if (nouveauTotal > montantTTC) {

            double resteDisponible =
                    Math.max(
                            montantTTC - totalActuel,
                            0.0
                    );

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant total des paiements dépasse "
                            + "le montant TTC de la facture. "
                            + "Montant restant disponible : "
                            + resteDisponible
                            + " DH"
            );
        }

        paiement.setFacture(facture);

        /*
         * On garde temporairement la relation Commande
         * pour ne pas casser les anciennes fonctionnalités.
         * Si elle n'est pas envoyée, on la récupère depuis :
         *
         * Facture -> BonLivraison -> Commande
         */
        if (
                paiement.getCommande() == null
                        && facture.getBonLivraison() != null
                        && facture.getBonLivraison()
                        .getCommande() != null
        ) {

            paiement.setCommande(
                    facture
                            .getBonLivraison()
                            .getCommande()
            );
        }
    }

    private void mettreAJourStatutFacture(
            Long factureId
    ) {

        Facture facture =
                factureRepository
                        .findById(factureId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Facture introuvable"
                                )
                        );

        double montantTTC =
                facture.getMontantTTC() != null
                        ? facture.getMontantTTC()
                        : 0.0;

        Double totalPaiements =
                paiementRepository
                        .getTotalPaiementsByFactureId(
                                factureId
                        );

        double montantPaye =
                totalPaiements != null
                        ? totalPaiements
                        : 0.0;

        if (montantPaye <= 0) {

            facture.setStatut(
                    "EMISE"
            );

        } else if (montantPaye < montantTTC) {

            facture.setStatut(
                    "PARTIELLEMENT_PAYEE"
            );

        } else {

            facture.setStatut(
                    "PAYEE"
            );
        }

        factureRepository.save(facture);
    }
}