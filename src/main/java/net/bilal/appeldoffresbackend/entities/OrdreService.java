package net.bilal.appeldoffresbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdreService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroOrdre;

    private LocalDate dateOrdre;

    private LocalDate dateDebutExecution;

    private String objet;

    private String statut;

    @ManyToOne
    @JoinColumn(name = "marche_id", nullable = false)
    private Marche marche;
}