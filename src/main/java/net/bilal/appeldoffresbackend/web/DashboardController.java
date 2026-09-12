package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.dtos.*;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.*;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashboardController {

    private final ClientRepository clientRepository;
    private final AppelDoffresRepository appelDoffresRepository;
    private final ConsultationRepository consultationRepository;
    private final MarcheRepository marcheRepository;
    private final CommandeRepository commandeRepository;
    private final PaiementRepository paiementRepository;
    private final FactureRepository factureRepository;
    private final OffreRepository offreRepository;
    private final OrdreServiceRepository ordreServiceRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public DashboardStatsDTO getDashboardStats(

            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) Integer mois
    ) {

        // =========================================================
        // VALIDATION DE LA PERIODE
        // =========================================================

        if (mois != null && annee == null) {
            throw new IllegalArgumentException(
                    "L'année est obligatoire lorsqu'un mois est sélectionné."
            );
        }

        if (mois != null && (mois < 1 || mois > 12)) {
            throw new IllegalArgumentException(
                    "Le mois doit être compris entre 1 et 12."
            );
        }


        boolean filtrePeriode = annee != null;

        LocalDate dateDebut = null;
        LocalDate dateFin = null;


        if (filtrePeriode) {

            if (mois != null) {

                // Exemple :
                // septembre 2026
                // 01/09/2026 <= date < 01/10/2026

                dateDebut = LocalDate.of(
                        annee,
                        mois,
                        1
                );

                dateFin = dateDebut.plusMonths(1);

            } else {

                // Exemple :
                // année 2026
                // 01/01/2026 <= date < 01/01/2027

                dateDebut = LocalDate.of(
                        annee,
                        1,
                        1
                );

                dateFin = dateDebut.plusYears(1);
            }
        }


        // =========================================================
        // CLIENTS
        //
        // Client ne possède actuellement pas de dateCreation.
        // Le nombre de clients reste donc GLOBAL.
        // =========================================================

        long totalClients =
                clientRepository.count();


        // =========================================================
        // APPELS D'OFFRES
        // Date de référence : datePublication
        // =========================================================

        long totalAppelsOffres;

        long aoAdjuges;

        long aoEnCours;

        long aoAnnules;

        double montantTotalAO;


        if (filtrePeriode) {

            totalAppelsOffres =
                    appelDoffresRepository.countByPeriode(
                            dateDebut,
                            dateFin
                    );

            aoAdjuges =
                    appelDoffresRepository
                            .countByStatutAndPeriode(
                                    "ADJUGE",
                                    dateDebut,
                                    dateFin
                            );

            aoEnCours =
                    appelDoffresRepository
                            .countByStatutAndPeriode(
                                    "EN_COURS",
                                    dateDebut,
                                    dateFin
                            );

            aoAnnules =
                    appelDoffresRepository
                            .countByStatutAndPeriode(
                                    "ANNULE",
                                    dateDebut,
                                    dateFin
                            );

            montantTotalAO =
                    appelDoffresRepository
                            .getMontantTotalByPeriode(
                                    dateDebut,
                                    dateFin
                            );

        } else {

            totalAppelsOffres =
                    appelDoffresRepository.count();

            aoAdjuges =
                    appelDoffresRepository
                            .countByStatut("ADJUGE");

            aoEnCours =
                    appelDoffresRepository
                            .countByStatut("EN_COURS");

            aoAnnules =
                    appelDoffresRepository
                            .countByStatut("ANNULE");

            montantTotalAO =
                    appelDoffresRepository.findAll()
                            .stream()
                            .filter(
                                    ao ->
                                            ao.getMontantEstime() != null
                            )
                            .mapToDouble(
                                    AppelDoffres::getMontantEstime
                            )
                            .sum();
        }


        // =========================================================
        // TAUX DE REUSSITE AO
        // =========================================================

        double tauxReussite = 0;

        if (totalAppelsOffres > 0) {

            tauxReussite =
                    ((double) aoAdjuges
                            / totalAppelsOffres)
                            * 100;
        }

        tauxReussite =
                Math.round(
                        tauxReussite * 100.0
                ) / 100.0;


        // =========================================================
        // CONSULTATIONS
        // Date de référence : dateReception
        // =========================================================

        long totalConsultations;

        long consultationsRetenues;

        long consultationsEnCours;

        long consultationsRefusees;


        if (filtrePeriode) {

            totalConsultations =
                    consultationRepository
                            .countByPeriode(
                                    dateDebut,
                                    dateFin
                            );

            consultationsRetenues =
                    consultationRepository
                            .countByStatutAndPeriode(
                                    "RETENUE",
                                    dateDebut,
                                    dateFin
                            );

            consultationsEnCours =
                    consultationRepository
                            .countByStatutAndPeriode(
                                    "EN_COURS",
                                    dateDebut,
                                    dateFin
                            );

            consultationsRefusees =
                    consultationRepository
                            .countByStatutAndPeriode(
                                    "REFUSEE",
                                    dateDebut,
                                    dateFin
                            );

        } else {

            totalConsultations =
                    consultationRepository.count();

            consultationsRetenues =
                    consultationRepository
                            .countByStatutIgnoreCase(
                                    "RETENUE"
                            );

            consultationsEnCours =
                    consultationRepository
                            .countByStatutIgnoreCase(
                                    "EN_COURS"
                            );

            consultationsRefusees =
                    consultationRepository
                            .countByStatutIgnoreCase(
                                    "REFUSEE"
                            );
        }


        // =========================================================
        // MARCHES
        // Date de référence : dateDebut
        // =========================================================

        long totalMarches;

        long marchesEnCours;

        long marchesTermines;

        double montantTotalMarches;


        if (filtrePeriode) {

            totalMarches =
                    marcheRepository
                            .countByPeriode(
                                    dateDebut,
                                    dateFin
                            );

            marchesEnCours =
                    marcheRepository
                            .countByStatutAndPeriode(
                                    "EN_COURS",
                                    dateDebut,
                                    dateFin
                            );

            marchesTermines =
                    marcheRepository
                            .countByStatutAndPeriode(
                                    "TERMINE",
                                    dateDebut,
                                    dateFin
                            );

            montantTotalMarches =
                    marcheRepository
                            .getMontantTotalByPeriode(
                                    dateDebut,
                                    dateFin
                            );

        } else {

            totalMarches =
                    marcheRepository.count();

            marchesEnCours =
                    marcheRepository
                            .countByStatutIgnoreCase(
                                    "EN_COURS"
                            );

            marchesTermines =
                    marcheRepository
                            .countByStatutIgnoreCase(
                                    "TERMINE"
                            );

            montantTotalMarches =
                    marcheRepository
                            .getMontantTotalMarches();
        }


        // =========================================================
        // COMMANDES
        // Date de référence : dateCommande
        // =========================================================

        long totalCommandes;

        long commandesEnCours;

        long commandesLivrees;

        double montantTotalCommandes;


        if (filtrePeriode) {

            totalCommandes =
                    commandeRepository
                            .countByPeriode(
                                    dateDebut,
                                    dateFin
                            );

            commandesEnCours =
                    commandeRepository
                            .countByStatutAndPeriode(
                                    "EN_COURS",
                                    dateDebut,
                                    dateFin
                            );

            commandesLivrees =
                    commandeRepository
                            .countByStatutAndPeriode(
                                    "LIVREE",
                                    dateDebut,
                                    dateFin
                            );

            montantTotalCommandes =
                    commandeRepository
                            .getMontantTotalByPeriode(
                                    dateDebut,
                                    dateFin
                            );

        } else {

            totalCommandes =
                    commandeRepository.count();

            commandesEnCours =
                    commandeRepository
                            .countByStatutIgnoreCase(
                                    "EN_COURS"
                            );

            commandesLivrees =
                    commandeRepository
                            .countByStatutIgnoreCase(
                                    "LIVREE"
                            );

            montantTotalCommandes =
                    commandeRepository
                            .getMontantTotalCommandes();
        }


        // =========================================================
        // PAIEMENTS
        // Date de référence : datePaiement
        // =========================================================

        long totalPaiements;

        long paiementsValides;

        long paiementsEnAttente;

        long paiementsAnnules;

        double totalEncaisse;


        if (filtrePeriode) {

            totalPaiements =
                    paiementRepository
                            .countByPeriode(
                                    dateDebut,
                                    dateFin
                            );

            paiementsValides =
                    paiementRepository
                            .countByStatutAndPeriode(
                                    "VALIDE",
                                    dateDebut,
                                    dateFin
                            );

            paiementsEnAttente =
                    paiementRepository
                            .countByStatutAndPeriode(
                                    "EN_ATTENTE",
                                    dateDebut,
                                    dateFin
                            );

            paiementsAnnules =
                    paiementRepository
                            .countByStatutAndPeriode(
                                    "ANNULE",
                                    dateDebut,
                                    dateFin
                            );

            // Seulement paiements VALIDES,
            // rattachés à une facture
            totalEncaisse =
                    paiementRepository
                            .getTotalEncaisseFacturesByPeriode(
                                    dateDebut,
                                    dateFin
                            );

        } else {

            totalPaiements =
                    paiementRepository.count();

            paiementsValides =
                    paiementRepository
                            .countByStatutIgnoreCase(
                                    "VALIDE"
                            );

            paiementsEnAttente =
                    paiementRepository
                            .countByStatutIgnoreCase(
                                    "EN_ATTENTE"
                            );

            paiementsAnnules =
                    paiementRepository
                            .countByStatutIgnoreCase(
                                    "ANNULE"
                            );

            totalEncaisse =
                    paiementRepository
                            .getTotalEncaisseFactures();
        }


        // =========================================================
        // PAIEMENT MOYEN
        // =========================================================

        double paiementMoyen = 0;


        if (paiementsValides > 0) {

            if (filtrePeriode) {

                paiementMoyen =
                        paiementRepository
                                .getMontantEncaisseByPeriode(
                                        dateDebut,
                                        dateFin
                                )
                                / paiementsValides;

            } else {

                paiementMoyen =
                        paiementRepository
                                .getChiffreAffaireValide()
                                / paiementsValides;
            }
        }


        paiementMoyen =
                Math.round(
                        paiementMoyen * 100.0
                ) / 100.0;


        // =========================================================
        // FACTURES
        // Date de référence : dateFacture
        // =========================================================

        double chiffreAffaireTotal;

        double montantTotalFacture;

        long totalFactures;

        long facturesPayees;

        long facturesPartiellementPayees;

        long facturesEmises;


        if (filtrePeriode) {

            chiffreAffaireTotal =
                    factureRepository
                            .getChiffreAffaireHTByPeriode(
                                    dateDebut,
                                    dateFin
                            );

            montantTotalFacture =
                    factureRepository
                            .getMontantFactureTTCByPeriode(
                                    dateDebut,
                                    dateFin
                            );

            totalFactures =
                    factureRepository
                            .countByPeriode(
                                    dateDebut,
                                    dateFin
                            );

            facturesPayees =
                    factureRepository
                            .countByStatutAndPeriode(
                                    "PAYEE",
                                    dateDebut,
                                    dateFin
                            );

            facturesPartiellementPayees =
                    factureRepository
                            .countByStatutAndPeriode(
                                    "PARTIELLEMENT_PAYEE",
                                    dateDebut,
                                    dateFin
                            );

            facturesEmises =
                    factureRepository
                            .countByStatutAndPeriode(
                                    "EMISE",
                                    dateDebut,
                                    dateFin
                            );

        } else {

            chiffreAffaireTotal =
                    factureRepository
                            .getChiffreAffaireTotalHT();

            montantTotalFacture =
                    factureRepository
                            .getTotalFacture();

            totalFactures =
                    factureRepository.count();

            facturesPayees =
                    factureRepository
                            .countByStatutIgnoreCase(
                                    "PAYEE"
                            );

            facturesPartiellementPayees =
                    factureRepository
                            .countByStatutIgnoreCase(
                                    "PARTIELLEMENT_PAYEE"
                            );

            facturesEmises =
                    factureRepository
                            .countByStatutIgnoreCase(
                                    "EMISE"
                            );
        }


        // =========================================================
        // RESTE A ENCAISSER
        // =========================================================

        double resteTotalAEncaisser;


        if (filtrePeriode) {

            /*
             * IMPORTANT :
             *
             * On prend les factures appartenant à la période,
             * puis on retire tous leurs paiements VALIDES.
             *
             * Ainsi, une facture d'août payée en septembre
             * reste rattachée à la facture d'août pour le calcul
             * du "reste à encaisser" de cette facture.
             */

            final LocalDate debut = dateDebut;
            final LocalDate fin = dateFin;


            resteTotalAEncaisser =
                    factureRepository.findAll()
                            .stream()

                            .filter(f ->
                                    f.getDateFacture() != null
                            )

                            .filter(f ->
                                    !f.getDateFacture()
                                            .isBefore(debut)
                            )

                            .filter(f ->
                                    f.getDateFacture()
                                            .isBefore(fin)
                            )

                            .filter(f ->
                                    f.getStatut() == null
                                            ||
                                            !f.getStatut()
                                                    .equalsIgnoreCase(
                                                            "ANNULEE"
                                                    )
                            )

                            .mapToDouble(f -> {

                                double montantTTC =
                                        f.getMontantTTC() != null
                                                ? f.getMontantTTC()
                                                : 0.0;


                                double dejaPaye =
                                        paiementRepository
                                                .getTotalPaiementsByFactureId(
                                                        f.getId()
                                                );


                                return Math.max(
                                        montantTTC - dejaPaye,
                                        0
                                );
                            })

                            .sum();

        } else {

            resteTotalAEncaisser =
                    montantTotalFacture
                            - totalEncaisse;
        }


        if (resteTotalAEncaisser < 0) {
            resteTotalAEncaisser = 0;
        }


        resteTotalAEncaisser =
                Math.round(
                        resteTotalAEncaisser * 100.0
                ) / 100.0;


        // =========================================================
        // TOP CLIENT AO
        // =========================================================

        String topClient;


        if (filtrePeriode) {

            final LocalDate debut = dateDebut;
            final LocalDate fin = dateFin;


            topClient =
                    appelDoffresRepository.findAll()
                            .stream()

                            .filter(ao ->
                                    ao.getDatePublication() != null
                            )

                            .filter(ao ->
                                    !ao.getDatePublication()
                                            .isBefore(debut)
                            )

                            .filter(ao ->
                                    ao.getDatePublication()
                                            .isBefore(fin)
                            )

                            .filter(ao ->
                                    ao.getClient() != null
                            )

                            .collect(
                                    java.util.stream.Collectors.groupingBy(
                                            ao ->
                                                    ao.getClient()
                                                            .getRaisonSociale(),

                                            java.util.stream.Collectors
                                                    .counting()
                                    )
                            )

                            .entrySet()
                            .stream()

                            .max(
                                    Map.Entry.comparingByValue()
                            )

                            .map(
                                    Map.Entry::getKey
                            )

                            .orElse(
                                    "Aucun client"
                            );

        } else {

            topClient =
                    appelDoffresRepository.findAll()
                            .stream()

                            .filter(ao ->
                                    ao.getClient() != null
                            )

                            .collect(
                                    java.util.stream.Collectors.groupingBy(
                                            ao ->
                                                    ao.getClient()
                                                            .getRaisonSociale(),

                                            java.util.stream.Collectors
                                                    .counting()
                                    )
                            )

                            .entrySet()
                            .stream()

                            .max(
                                    Map.Entry.comparingByValue()
                            )

                            .map(
                                    Map.Entry::getKey
                            )

                            .orElse(
                                    "Aucun client"
                            );
        }


        // =========================================================
        // AO URGENTS / EN RETARD
        // =========================================================

        LocalDate today =
                LocalDate.now();


        long aoEnRetard;

        long aoUrgents;


        if (filtrePeriode) {

            final LocalDate debut = dateDebut;
            final LocalDate fin = dateFin;


            aoEnRetard =
                    appelDoffresRepository.findAll()
                            .stream()

                            .filter(ao ->
                                    ao.getDatePublication() != null
                            )

                            .filter(ao ->
                                    !ao.getDatePublication()
                                            .isBefore(debut)
                            )

                            .filter(ao ->
                                    ao.getDatePublication()
                                            .isBefore(fin)
                            )

                            .filter(ao ->
                                    ao.getDateLimite() != null
                            )

                            .filter(ao ->
                                    ao.getDateLimite()
                                            .isBefore(today)
                            )

                            .count();


            aoUrgents =
                    appelDoffresRepository.findAll()
                            .stream()

                            .filter(ao ->
                                    ao.getDatePublication() != null
                            )

                            .filter(ao ->
                                    !ao.getDatePublication()
                                            .isBefore(debut)
                            )

                            .filter(ao ->
                                    ao.getDatePublication()
                                            .isBefore(fin)
                            )

                            .filter(ao ->
                                    ao.getDateLimite() != null
                            )

                            .filter(ao ->
                                    !ao.getDateLimite()
                                            .isBefore(today)
                            )

                            .filter(ao ->
                                    ao.getDateLimite()
                                            .isBefore(
                                                    today.plusDays(8)
                                            )
                            )

                            .count();

        } else {

            aoEnRetard =
                    appelDoffresRepository.findAll()
                            .stream()

                            .filter(ao ->
                                    ao.getDateLimite() != null
                                            &&
                                            ao.getDateLimite()
                                                    .isBefore(today)
                            )

                            .count();


            aoUrgents =
                    appelDoffresRepository.findAll()
                            .stream()

                            .filter(ao ->
                                    ao.getDateLimite() != null
                                            &&
                                            !ao.getDateLimite()
                                                    .isBefore(today)
                                            &&
                                            ao.getDateLimite()
                                                    .isBefore(
                                                            today.plusDays(8)
                                                    )
                            )

                            .count();
        }


        // =========================================================
        // DTO
        // =========================================================

        return new DashboardStatsDTO(

                totalClients,

                totalAppelsOffres,

                totalConsultations,

                consultationsRetenues,

                consultationsEnCours,

                consultationsRefusees,

                totalMarches,

                marchesEnCours,

                marchesTermines,

                montantTotalMarches,

                totalCommandes,

                commandesEnCours,

                commandesLivrees,

                montantTotalCommandes,

                totalPaiements,

                paiementsValides,

                paiementsEnAttente,

                paiementsAnnules,

                paiementMoyen,

                chiffreAffaireTotal,

                aoAdjuges,

                tauxReussite,

                aoEnCours,

                aoAnnules,

                montantTotalAO,

                topClient,

                aoEnRetard,

                aoUrgents,

                totalFactures,

                facturesPayees,

                facturesPartiellementPayees,

                facturesEmises,

                montantTotalFacture,

                resteTotalAEncaisser,

                totalEncaisse
        );
    }

    @GetMapping("/stats-das")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<DashboardDasDTO> getStatsParDas(
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) Integer mois
    ) {

        if (mois != null && annee == null) {
            throw new IllegalArgumentException(
                    "L'année est obligatoire lorsqu'un mois est sélectionné"
            );
        }

        if (mois != null && (mois < 1 || mois > 12)) {
            throw new IllegalArgumentException(
                    "Le mois doit être compris entre 1 et 12"
            );
        }

        LocalDate dateDebut = null;
        LocalDate dateFin = null;

        if (annee != null) {

            if (mois != null) {
                dateDebut = LocalDate.of(annee, mois, 1);
                dateFin = dateDebut.plusMonths(1);
            } else {
                dateDebut = LocalDate.of(annee, 1, 1);
                dateFin = dateDebut.plusYears(1);
            }
        }

        final LocalDate debut = dateDebut;
        final LocalDate fin = dateFin;
        final boolean filtrePeriode = annee != null;

        return java.util.Arrays.stream(Das.values())
                .map(das -> {

                    long nombreAppelsOffres;
                    double montantAppelsOffres;

                    long nombreConsultations;
                    double montantConsultations;

                    long nombreOffres;
                    double montantOffres;

                    long nombreMarches;
                    double montantMarches;

                    long nombreOrdresService;

                    long nombreCommandes;
                    double montantCommandes;

                    double montantFacture;
                    double montantEncaisse;
                    double totalPaiementsFacturesPeriode;

                    if (filtrePeriode) {

                        nombreAppelsOffres =
                                appelDoffresRepository
                                        .countByDasAndPeriode(
                                                das, debut, fin
                                        );

                        montantAppelsOffres =
                                appelDoffresRepository
                                        .getMontantTotalByDasAndPeriode(
                                                das, debut, fin
                                        );

                        nombreConsultations =
                                consultationRepository
                                        .countByDasAndPeriode(
                                                das, debut, fin
                                        );

                        montantConsultations =
                                consultationRepository
                                        .getMontantTotalByDasAndPeriode(
                                                das, debut, fin
                                        );

                        nombreOffres =
                                offreRepository
                                        .countByDasAndPeriode(
                                                das, debut, fin
                                        );

                        montantOffres =
                                offreRepository
                                        .getMontantTotalByDasAndPeriode(
                                                das, debut, fin
                                        );

                        nombreMarches =
                                marcheRepository
                                        .countByDasAndPeriode(
                                                das, debut, fin
                                        );

                        montantMarches =
                                marcheRepository
                                        .getMontantTotalByDasAndPeriode(
                                                das, debut, fin
                                        );

                        nombreOrdresService =
                                ordreServiceRepository
                                        .countByMarcheDasAndPeriode(
                                                das, debut, fin
                                        );

                        nombreCommandes =
                                commandeRepository
                                        .countByDasAndPeriode(
                                                das, debut, fin
                                        );

                        montantCommandes =
                                commandeRepository
                                        .getMontantTotalByDasAndPeriode(
                                                das, debut, fin
                                        );

                        montantFacture =
                                factureRepository
                                        .getMontantTotalByDasAndPeriode(
                                                das, debut, fin
                                        );

                        montantEncaisse =
                                paiementRepository
                                        .getMontantEncaisseByDasAndPeriode(
                                                das, debut, fin
                                        );

                        totalPaiementsFacturesPeriode =
                                paiementRepository
                                        .getTotalPaiementsFacturesByDasAndPeriode(
                                                das, debut, fin
                                        );

                    } else {

                        // Ancien comportement global
                        nombreAppelsOffres =
                                appelDoffresRepository.countByDas(das);

                        montantAppelsOffres =
                                appelDoffresRepository
                                        .getMontantTotalByDas(das);

                        nombreConsultations =
                                consultationRepository.countByDas(das);

                        montantConsultations =
                                consultationRepository
                                        .getMontantTotalByDas(das);

                        nombreOffres =
                                offreRepository.countByDas(das);

                        montantOffres =
                                offreRepository
                                        .getMontantTotalByDas(das);

                        nombreMarches =
                                marcheRepository.countByDas(das);

                        montantMarches =
                                marcheRepository
                                        .getMontantTotalByDas(das);

                        nombreOrdresService =
                                ordreServiceRepository
                                        .countByMarcheDas(das);

                        nombreCommandes =
                                commandeRepository.countByDas(das);

                        montantCommandes =
                                commandeRepository
                                        .getMontantTotalByDas(das);

                        montantFacture =
                                factureRepository
                                        .getMontantTotalByDas(das);

                        montantEncaisse =
                                paiementRepository
                                        .getMontantEncaisseByDas(das);

                        totalPaiementsFacturesPeriode = montantEncaisse;
                    }

                    double resteAEncaisser;

                    if (filtrePeriode) {
                        resteAEncaisser =
                                Math.max(
                                        montantFacture
                                                - totalPaiementsFacturesPeriode,
                                        0.0
                                );
                    } else {
                        resteAEncaisser =
                                Math.max(
                                        montantFacture
                                                - montantEncaisse,
                                        0.0
                                );
                    }

                    resteAEncaisser =
                            Math.round(
                                    resteAEncaisser * 100.0
                            ) / 100.0;

                    return new DashboardDasDTO(
                            das.name(),
                            nombreAppelsOffres,
                            montantAppelsOffres,
                            nombreConsultations,
                            montantConsultations,
                            nombreOffres,
                            montantOffres,
                            nombreMarches,
                            montantMarches,
                            nombreOrdresService,
                            nombreCommandes,
                            montantCommandes,
                            montantFacture,
                            montantEncaisse,
                            resteAEncaisser
                    );
                })
                .toList();
    }

    @GetMapping("/alertes/appels-offres")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<AlerteAppelOffreDTO> getAOUrgents() {

        LocalDate today = LocalDate.now();

        return appelDoffresRepository
                .getAppelsOffresUrgents(today.plusDays(7))
                .stream()
                .map(ao -> {

                    long joursRestants =
                            ao.getDateLimite() != null
                                    ? ChronoUnit.DAYS.between(
                                    today,
                                    ao.getDateLimite()
                            )
                                    : 0;

                    String etatAlerte;

                    if (joursRestants < 0) {
                        etatAlerte = "EN_RETARD";
                    } else if (joursRestants <= 7) {
                        etatAlerte = "URGENT";
                    } else {
                        etatAlerte = "NORMAL";
                    }

                    return new AlerteAppelOffreDTO(
                            ao.getId(),
                            ao.getReference(),
                            ao.getObjet(),
                            ao.getDateLimite(),
                            joursRestants,
                            ao.getStatut(),
                            ao.getDas(),
                            etatAlerte
                    );
                })
                .toList();
    }

    @GetMapping("/chiffre-affaire-mensuel")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<ChiffreAffaireMensuelDTO> getChiffreAffaireMensuel(
            @RequestParam(required = false) Integer annee
    ) {

        if (annee != null) {
            return factureRepository
                    .getChiffreAffaireMensuelHTByAnnee(annee);
        }

        return factureRepository.getChiffreAffaireMensuelHT();
    }

    @GetMapping("/top-clients")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<TopClientDTO> getTopClients(
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) Integer mois
    ) {

        if (annee == null) {
            return factureRepository.getTopClientsByChiffreAffaireHT();
        }

        if (mois != null && (mois < 1 || mois > 12)) {
            throw new IllegalArgumentException(
                    "Le mois doit être compris entre 1 et 12"
            );
        }

        LocalDate dateDebut;
        LocalDate dateFin;

        if (mois != null) {

            dateDebut = LocalDate.of(annee, mois, 1);
            dateFin = dateDebut.plusMonths(1);

        } else {

            dateDebut = LocalDate.of(annee, 1, 1);
            dateFin = dateDebut.plusYears(1);
        }

        return factureRepository
                .getTopClientsByChiffreAffaireHTPeriode(
                        dateDebut,
                        dateFin
                );
    }

    @GetMapping("/top-appels-offres")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<TopAppelOffreDTO> getTopAppelsOffres() {

        return appelDoffresRepository
                .getTopAppelsOffres(
                        PageRequest.of(0, 5)
                );
    }

    @GetMapping("/chiffre-affaire-annuel")
    public List<Map<String, Object>> getChiffreAffaireAnnuel() {

        List<Object[]> resultats =
                factureRepository.getChiffreAffaireHTParAnnee();

        return resultats.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("annee", row[0]);
                    item.put("total", row[1]);
                    return item;
                })
                .toList();
    }

}