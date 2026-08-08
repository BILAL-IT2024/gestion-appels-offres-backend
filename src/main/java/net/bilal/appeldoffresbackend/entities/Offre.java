package net.bilal.appeldoffresbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import net.bilal.appeldoffresbackend.enums.Das;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    private LocalDate dateOffre;

    @Column(nullable = false)
    private Double montantOffre;

    private String statut;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Das das;

    @ManyToOne
    @JoinColumn(name = "appel_doffres_id")
    private AppelDoffres appelDoffres;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;
}