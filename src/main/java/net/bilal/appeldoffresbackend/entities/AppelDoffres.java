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
public class AppelDoffres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;

    private String objet;

    private LocalDate datePublication;

    private LocalDate dateLimite;

    private Double montantEstime;

    private String statut;

    @ManyToOne
    private Client client;
}
