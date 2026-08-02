package net.bilal.appeldoffresbackend.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import net.bilal.appeldoffresbackend.enums.Das;

import java.time.LocalDate;

@JsonPropertyOrder({
        "id",
        "reference",
        "objet",
        "montantEstime",
        "statut",
        "das",
        "clientNom"
})
@Data
public class AppelDoffresDTO {

    private Long id;

    private String reference;

    private String objet;

    private Double montantEstime;

    private String statut;

    private Das das;

    private String clientNom;

    private LocalDate datePublication;

    private LocalDate dateLimite;

    private Long clientId;


}
