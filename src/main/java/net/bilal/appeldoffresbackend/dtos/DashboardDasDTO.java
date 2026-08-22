package net.bilal.appeldoffresbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDasDTO {

    private String das;

    private long nombreAppelsOffres;
    private double montantAppelsOffres;

    private long nombreConsultations;
    private double montantConsultations;

    private long nombreOffres;
    private double montantOffres;

    private long nombreMarches;
    private double montantMarches;

    private long nombreOrdresService;

    private long nombreCommandes;
    private double montantCommandes;

    private double montantFacture;

    private double montantEncaisse;

    private double resteAEncaisser;
}