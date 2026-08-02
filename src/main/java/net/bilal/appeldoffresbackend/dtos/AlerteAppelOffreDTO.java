package net.bilal.appeldoffresbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bilal.appeldoffresbackend.enums.Das;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlerteAppelOffreDTO {

    private Long id;

    private String reference;

    private String objet;

    private LocalDate dateLimite;

    private long joursRestants;

    private String statut;

    private Das das;

    private String etatAlerte;
}