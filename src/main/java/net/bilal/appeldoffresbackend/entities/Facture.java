package net.bilal.appeldoffresbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroFacture;

    private LocalDate dateFacture;

    private LocalDate dateEcheance;

    private Double montantHT;

    private Double tva;

    private Double montantTTC;

    private String statut;

    @ManyToOne
    @JoinColumn(name = "bon_livraison_id", nullable = false)
    private BonLivraison bonLivraison;
}