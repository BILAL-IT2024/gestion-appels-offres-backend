package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.BonLivraison;
import net.bilal.appeldoffresbackend.entities.Facture;
import net.bilal.appeldoffresbackend.repositories.BonLivraisonRepository;
import net.bilal.appeldoffresbackend.repositories.FactureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        verifierMontantFacturable(facture);

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

        double ancienMontantTTC =
                factureExistante.getMontantTTC() != null
                        ? factureExistante.getMontantTTC()
                        : 0.0;

        Long ancienBonLivraisonId =
                factureExistante.getBonLivraison() != null
                        ? factureExistante.getBonLivraison().getId()
                        : null;

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

        verifierMontantFacturableModification(
                factureExistante,
                ancienMontantTTC,
                ancienBonLivraisonId
        );

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

    public Map<String, Double> getResumeFacturation(
            Long bonLivraisonId
    ) {

        BonLivraison bonLivraison =
                bonLivraisonRepository
                        .findById(bonLivraisonId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Bon de livraison introuvable"
                                )
                        );

        double montantLivre =
                bonLivraison.getMontantLivraison() != null
                        ? bonLivraison.getMontantLivraison()
                        : 0.0;

        Double totalFacture =
                factureRepository
                        .getTotalFactureByBonLivraisonId(
                                bonLivraisonId
                        );

        double montantFacture =
                totalFacture != null
                        ? totalFacture
                        : 0.0;

        double montantRestant =
                Math.max(
                        montantLivre - montantFacture,
                        0.0
                );

        Map<String, Double> resume =
                new HashMap<>();

        resume.put(
                "montantLivre",
                montantLivre
        );

        resume.put(
                "montantFacture",
                montantFacture
        );

        resume.put(
                "montantRestant",
                montantRestant
        );

        return resume;
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

    private void verifierMontantFacturable(
            Facture facture
    ) {

        BonLivraison bonLivraison =
                facture.getBonLivraison();

        if (
                bonLivraison == null
                        || bonLivraison.getId() == null
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Veuillez sélectionner un bon de livraison"
            );
        }

        if (
                bonLivraison.getMontantLivraison() == null
                        || bonLivraison.getMontantLivraison() <= 0
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le bon de livraison ne possède pas de montant livrable"
            );
        }

        Double totalFacture =
                factureRepository
                        .getTotalFactureByBonLivraisonId(
                                bonLivraison.getId()
                        );

        double totalActuel =
                totalFacture != null
                        ? totalFacture
                        : 0.0;

        double nouveauTotal =
                totalActuel
                        + facture.getMontantTTC();

        double montantLivraison =
                bonLivraison.getMontantLivraison();

        if (nouveauTotal > montantLivraison) {

            double montantRestant =
                    Math.max(
                            montantLivraison - totalActuel,
                            0.0
                    );

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant total facturé dépasse le montant livré. "
                            + "Montant restant à facturer : "
                            + montantRestant
                            + " DH"
            );
        }
    }

    private void verifierMontantFacturableModification(
            Facture facture,
            double ancienMontantTTC,
            Long ancienBonLivraisonId
    ) {

        BonLivraison bonLivraison =
                facture.getBonLivraison();

        if (
                bonLivraison == null
                        || bonLivraison.getId() == null
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Veuillez sélectionner un bon de livraison"
            );
        }

        if (
                bonLivraison.getMontantLivraison() == null
                        || bonLivraison.getMontantLivraison() <= 0
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le bon de livraison ne possède pas de montant livrable"
            );
        }

        Long nouveauBonLivraisonId =
                bonLivraison.getId();

        Double totalFacture =
                factureRepository
                        .getTotalFactureByBonLivraisonId(
                                nouveauBonLivraisonId
                        );

        double totalActuel =
                totalFacture != null
                        ? totalFacture
                        : 0.0;

        boolean memeBonLivraison =
                ancienBonLivraisonId != null
                        && ancienBonLivraisonId.equals(
                        nouveauBonLivraisonId
                );

        double totalSansFactureActuelle =
                memeBonLivraison
                        ? totalActuel - ancienMontantTTC
                        : totalActuel;

        double nouveauTotal =
                totalSansFactureActuelle
                        + facture.getMontantTTC();

        double montantLivraison =
                bonLivraison.getMontantLivraison();

        if (nouveauTotal > montantLivraison) {

            double montantDisponible =
                    Math.max(
                            montantLivraison
                                    - totalSansFactureActuelle,
                            0.0
                    );

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant total facturé dépasse le montant livré. "
                            + "Montant disponible pour cette facture : "
                            + montantDisponible
                            + " DH"
            );
        }
    }

}