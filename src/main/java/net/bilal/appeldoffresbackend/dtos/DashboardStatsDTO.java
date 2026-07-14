package net.bilal.appeldoffresbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {

    private long totalClients;

    private long totalAppelsOffres;

    private long totalConsultations;

    private long consultationsRetenues;

    private long consultationsEnCours;

    private long consultationsRefusees;

    private long totalMarches;

    private long marchesEnCours;

    private long marchesTermines;

    private double montantTotalMarches;

    private long totalCommandes;

    private long totalPaiements;

    private double chiffreAffaireTotal;

    private long aoAdjuges;

    private double tauxReussite;

    private long aoEnCours;

    private long aoAnnules;

    private double montantTotalAO;

    private String topClient;

    private long aoEnRetard;

    private long aoUrgents;
}
