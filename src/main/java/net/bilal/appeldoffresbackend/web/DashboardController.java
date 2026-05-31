package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public Map<String, Object> getDashboardStats() {

        Map<String, Object> stats = new LinkedHashMap<>();;

        stats.put("totalClients",
                clientRepository.count());

        stats.put("totalAppelsOffres",
                appelDoffresRepository.count());

        stats.put("totalConsultations",
                consultationRepository.count());

        stats.put("totalMarches",
                marcheRepository.count());

        stats.put("totalCommandes",
                commandeRepository.count());

        stats.put("totalPaiements",
                paiementRepository.count());

        stats.put("chiffreAffaireTotal",
                paiementRepository.getTotalChiffreAffaire());

        long totalAO = appelDoffresRepository.count();

        long aoAdjuges =
                appelDoffresRepository.countBystatut("ADJUGE");

        double tauxReussite = 0;

        if (totalAO > 0) {
            tauxReussite =
                    ((double) aoAdjuges / totalAO) * 100;
        }

        stats.put("aoAdjuges", aoAdjuges);

        stats.put("tauxReussite", tauxReussite);

        return stats;
    }

    @GetMapping("/alertes/appels-offres")
    public List<AppelDoffres> getAOUrgents() {

        return appelDoffresRepository
                .getAppelsOffresUrgents(LocalDate.now().plusDays(7));
    }
}
