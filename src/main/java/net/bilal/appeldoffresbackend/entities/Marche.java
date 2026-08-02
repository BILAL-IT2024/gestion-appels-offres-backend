package net.bilal.appeldoffresbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bilal.appeldoffresbackend.enums.Das;

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

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Das das;

    @OneToOne
    private AppelDoffres appelDoffres;
}
