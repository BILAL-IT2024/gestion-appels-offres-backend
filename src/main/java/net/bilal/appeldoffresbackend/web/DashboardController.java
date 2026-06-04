package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.dtos.AlerteAppelOffreDTO;
import net.bilal.appeldoffresbackend.dtos.DashboardStatsDTO;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import net.bilal.appeldoffresbackend.dtos.ChiffreAffaireMensuelDTO;
import net.bilal.appeldoffresbackend.dtos.TopClientDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public DashboardStatsDTO getDashboardStats() {

        long totalClients = clientRepository.count();
        long totalAppelsOffres = appelDoffresRepository.count();
        long totalConsultations = consultationRepository.count();
        long totalMarches = marcheRepository.count();
        long totalCommandes = commandeRepository.count();
        long totalPaiements = paiementRepository.count();

        double chiffreAffaireTotal =
                paiementRepository.getTotalChiffreAffaire();

        long totalAO = appelDoffresRepository.count();

        long aoAdjuges =
                appelDoffresRepository.countByStatut("ADJUGE");

        double tauxReussite = 0;

        if (totalAO > 0) {
            tauxReussite =
                    ((double) aoAdjuges / totalAO) * 100;
        }

        tauxReussite =
                Math.round(tauxReussite * 100.0) / 100.0;

        long aoEnCours =
                appelDoffresRepository.countByStatut("EN_COURS");

        long aoAnnules =
                appelDoffresRepository.countByStatut("ANNULE");

        double montantTotalAO =
                appelDoffresRepository.findAll()
                        .stream()
                        .filter(ao -> ao.getMontantEstime() != null)
                        .mapToDouble(AppelDoffres::getMontantEstime)
                        .sum();

        String topClient =
                appelDoffresRepository.findAll()
                        .stream()
                        .filter(ao -> ao.getClient() != null)
                        .collect(
                                java.util.stream.Collectors.groupingBy(
                                        ao -> ao.getClient().getRaisonSociale(),
                                        java.util.stream.Collectors.counting()
                                )
                        )
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("Aucun client");

        LocalDate today = LocalDate.now();

        long aoEnRetard =
                appelDoffresRepository.findAll()
                        .stream()
                        .filter(ao ->
                                ao.getDateLimite() != null
                                        &&
                                        ao.getDateLimite().isBefore(today)
                        )
                        .count();

        long aoUrgents =
                appelDoffresRepository.findAll()
                        .stream()
                        .filter(ao ->
                                ao.getDateLimite() != null
                                        &&
                                        !ao.getDateLimite().isBefore(today)
                                        &&
                                        ao.getDateLimite()
                                                .isBefore(today.plusDays(8))
                        )
                        .count();

        return new DashboardStatsDTO(
                totalClients,
                totalAppelsOffres,
                totalConsultations,
                totalMarches,
                totalCommandes,
                totalPaiements,
                chiffreAffaireTotal,
                aoAdjuges,
                tauxReussite,
                aoEnCours,
                aoAnnules,
                montantTotalAO,
                topClient,
                aoEnRetard,
                aoUrgents
        );
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
                            etatAlerte
                    );
                })
                .toList();
    }

    @GetMapping("/chiffre-affaire-mensuel")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<ChiffreAffaireMensuelDTO> getChiffreAffaireMensuel() {

        return paiementRepository.getChiffreAffaireMensuel();
    }

    @GetMapping("/top-clients")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<TopClientDTO> getTopClients() {

        return paiementRepository.getTopClients();
    }
}