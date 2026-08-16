package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Commande;
import net.bilal.appeldoffresbackend.repositories.CommandeRepository;
import org.springframework.stereotype.Service;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.repositories.MarcheRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import net.bilal.appeldoffresbackend.entities.Consultation;
import net.bilal.appeldoffresbackend.repositories.ConsultationRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final MarcheRepository marcheRepository;
    private final ConsultationRepository consultationRepository;

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id).orElse(null);
    }

    public Commande saveCommande(Commande commande) {

        boolean hasMarche =
                commande.getMarche() != null
                        && commande.getMarche().getId() != null;

        boolean hasConsultation =
                commande.getConsultation() != null
                        && commande.getConsultation().getId() != null;

        if (!hasMarche && !hasConsultation) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Veuillez sélectionner un marché ou une consultation retenue"
            );
        }

        if (hasMarche && hasConsultation) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Une commande ne peut pas être liée à un marché et une consultation en même temps"
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

        /*
         * CAS 1 : commande issue d'un marché
         */
        if (hasMarche) {

            Long marcheId =
                    commande.getMarche().getId();

            Marche marche =
                    marcheRepository
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
                                    marcheId
                            );

            double totalActuel =
                    totalCommandes != null
                            ? totalCommandes
                            : 0.0;

            double nouveauTotal =
                    totalActuel
                            + commande.getMontantCommande();

            if (nouveauTotal > marche.getMontantMarche()) {

                double montantRestant =
                        marche.getMontantMarche()
                                - totalActuel;

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Le montant total des commandes dépasse le montant du marché. "
                                + "Montant restant disponible : "
                                + montantRestant
                                + " DH"
                );
            }

            commande.setMarche(marche);
            commande.setConsultation(null);
        }

        /*
         * CAS 2 : commande issue d'une consultation retenue
         */
        if (hasConsultation) {

            Long consultationId =
                    commande.getConsultation().getId();

            Consultation consultation =
                    consultationRepository
                            .findById(consultationId)
                            .orElseThrow(() ->
                                    new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                                            "Consultation introuvable"
                                    )
                            );

            if (
                    consultation.getStatut() == null
                            || !consultation.getStatut()
                            .equalsIgnoreCase("RETENUE")
            ) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La consultation sélectionnée doit être RETENUE"
                );
            }

            if (consultation.getMontantPropose() == null) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La consultation sélectionnée ne possède pas de montant"
                );
            }

            BigDecimal totalCommandes =
                    commandeRepository
                            .totalCommandesParConsultation(
                                    consultationId
                            );

            double totalActuel =
                    totalCommandes != null
                            ? totalCommandes.doubleValue()
                            : 0.0;

            double nouveauTotal =
                    totalActuel
                            + commande.getMontantCommande();

            if (
                    nouveauTotal
                            > consultation.getMontantPropose()
            ) {

                double montantRestant =
                        consultation.getMontantPropose()
                                - totalActuel;

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Le montant total des commandes dépasse le montant de la consultation. "
                                + "Montant restant disponible : "
                                + montantRestant
                                + " DH"
                );
            }

            commande.setConsultation(consultation);
            commande.setMarche(null);
        }

        return commandeRepository.save(commande);
    }

    public Commande updateCommande(
            Long id,
            Commande commande
    ) {

        Commande ancienneCommande =
                commandeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Commande introuvable"
                                )
                        );

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

        Long nouveauMarcheId =
                commande.getMarche().getId();

        Marche nouveauMarche =
                marcheRepository
                        .findById(nouveauMarcheId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Marché introuvable"
                                )
                        );

        if (nouveauMarche.getMontantMarche() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le marché sélectionné ne possède pas de montant"
            );
        }

        Double totalCommandes =
                commandeRepository
                        .getTotalCommandesByMarcheId(
                                nouveauMarcheId
                        );

        double totalActuel =
                totalCommandes != null
                        ? totalCommandes
                        : 0.0;

        boolean memeMarche =
                ancienneCommande.getMarche() != null
                        && ancienneCommande
                        .getMarche()
                        .getId()
                        .equals(nouveauMarcheId);

        double totalSansCommandeActuelle =
                memeMarche
                        ? totalActuel
                        - ancienneCommande.getMontantCommande()
                        : totalActuel;

        double nouveauTotal =
                totalSansCommandeActuelle
                        + commande.getMontantCommande();

        double montantMarche =
                nouveauMarche.getMontantMarche();

        if (nouveauTotal > montantMarche) {

            double montantRestant =
                    montantMarche
                            - totalSansCommandeActuelle;

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant total des commandes dépasse "
                            + "le montant du marché. "
                            + "Montant disponible pour cette commande : "
                            + montantRestant
                            + " DH"
            );
        }

        commande.setId(id);
        commande.setMarche(nouveauMarche);

        return commandeRepository.save(commande);
    }

    public List<Commande> rechercherCommandes(String keyword) {
        return commandeRepository.findByNumeroCommandeContainingIgnoreCase(keyword);
    }

    public Map<String, Double> getResumeMarche(Long marcheId) {

        Marche marche = marcheRepository
                .findById(marcheId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Marché introuvable"
                        )
                );

        Double totalCommandes =
                commandeRepository
                        .getTotalCommandesByMarcheId(marcheId);

        double montantMarche =
                marche.getMontantMarche() != null
                        ? marche.getMontantMarche()
                        : 0.0;

        double montantCommande =
                totalCommandes != null
                        ? totalCommandes
                        : 0.0;

        double montantRestant =
                Math.max(
                        montantMarche - montantCommande,
                        0.0
                );

        return Map.of(
                "montantMarche", montantMarche,
                "montantCommande", montantCommande,
                "montantRestant", montantRestant
        );
    }

    public Map<String, Double> getResumeConsultation(
            Long consultationId
    ) {

        Consultation consultation =
                consultationRepository
                        .findById(consultationId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Consultation introuvable"
                                )
                        );

        BigDecimal totalCommandes =
                commandeRepository
                        .totalCommandesParConsultation(
                                consultationId
                        );

        double montantConsultation =
                consultation.getMontantPropose() != null
                        ? consultation.getMontantPropose()
                        : 0.0;

        double montantCommande =
                totalCommandes != null
                        ? totalCommandes.doubleValue()
                        : 0.0;

        double montantRestant =
                Math.max(
                        montantConsultation - montantCommande,
                        0.0
                );

        return Map.of(
                "montantConsultation", montantConsultation,
                "montantCommande", montantCommande,
                "montantRestant", montantRestant
        );
    }

    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }
}
