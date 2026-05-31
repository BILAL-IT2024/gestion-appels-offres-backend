package net.bilal.appeldoffresbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroMarche;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private Double montantMarche;

    private Double tauxExecution;

    private String statut;

    @OneToOne
    private AppelDoffres appelDoffres;
}
