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
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datePaiement;

    private Double montantPaiement;

    private String modePaiement;

    private String referencePaiement;

    private String statut;

    @ManyToOne
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture;

}
