package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.BonLivraison;
import net.bilal.appeldoffresbackend.entities.Facture;
import net.bilal.appeldoffresbackend.repositories.BonLivraisonRepository;
import net.bilal.appeldoffresbackend.repositories.FactureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepository;
    private final BonLivraisonRepository bonLivraisonRepository;


    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }


    public Facture getFactureById(Long id) {

        return factureRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Facture introuvable"
                        )
                );
    }


    public Facture saveFacture(Facture facture) {

        verifierBonLivraison(facture);

        calculerMontantTTC(facture);

        if (
                facture.getStatut() == null
                        || facture.getStatut().isBlank()
        ) {
            facture.setStatut("BROUILLON");
        }

        return factureRepository.save(facture);
    }


    public Facture updateFacture(
            Long id,
            Facture facture
    ) {

        Facture factureExistante =
                getFactureById(id);

        factureExistante.setNumeroFacture(
                facture.getNumeroFacture()
        );

        factureExistante.setDateFacture(
                facture.getDateFacture()
        );

        factureExistante.setDateEcheance(
                facture.getDateEcheance()
        );

        factureExistante.setMontantHT(
                facture.getMontantHT()
        );

        factureExistante.setTva(
                facture.getTva()
        );

        factureExistante.setStatut(
                facture.getStatut()
        );

        if (
                facture.getBonLivraison() != null
                        && facture.getBonLivraison().getId() != null
        ) {

            BonLivraison bonLivraison =
                    bonLivraisonRepository
                            .findById(
                                    facture
                                            .getBonLivraison()
                                            .getId()
                            )
                            .orElseThrow(() ->
                                    new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                                            "Bon de livraison introuvable"
                                    )
                            );

            factureExistante.setBonLivraison(
                    bonLivraison
            );
        }

        calculerMontantTTC(factureExistante);

        return factureRepository.save(
                factureExistante
        );
    }


    public List<Facture> rechercherFactures(
            String keyword
    ) {

        return factureRepository
                .findByNumeroFactureContainingIgnoreCase(
                        keyword
                );
    }


    public List<Facture> getFacturesByBonLivraison(
            Long bonLivraisonId
    ) {

        return factureRepository
                .findByBonLivraisonId(
                        bonLivraisonId
                );
    }


    public List<Facture> getFacturesByStatut(
            String statut
    ) {

        return factureRepository
                .findByStatutIgnoreCase(statut);
    }


    public void deleteFacture(Long id) {

        if (!factureRepository.existsById(id)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Facture introuvable"
            );
        }

        factureRepository.deleteById(id);
    }


    private void verifierBonLivraison(
            Facture facture
    ) {

        if (
                facture.getBonLivraison() == null
                        || facture.getBonLivraison().getId() == null
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Veuillez sélectionner un bon de livraison"
            );
        }

        BonLivraison bonLivraison =
                bonLivraisonRepository
                        .findById(
                                facture
                                        .getBonLivraison()
                                        .getId()
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Bon de livraison introuvable"
                                )
                        );

        facture.setBonLivraison(
                bonLivraison
        );
    }


    private void calculerMontantTTC(
            Facture facture
    ) {

        if (facture.getMontantHT() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant HT est obligatoire"
            );
        }

        double tauxTva =
                facture.getTva() != null
                        ? facture.getTva()
                        : 0.0;

        double montantTTC =
                facture.getMontantHT()
                        * (1 + tauxTva / 100);

        facture.setMontantTTC(
                Math.round(montantTTC * 100.0)
                        / 100.0
        );
    }
}