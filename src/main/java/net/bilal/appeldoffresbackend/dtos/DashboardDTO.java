package net.bilal.appeldoffresbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {

    private long totalAppelsOffres;

    private long enCours;

    private long adjuges;

    private long annules;

    private double montantTotal;
}
