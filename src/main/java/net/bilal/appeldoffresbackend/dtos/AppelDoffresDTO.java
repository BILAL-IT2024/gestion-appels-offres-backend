package net.bilal.appeldoffresbackend.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonPropertyOrder({
        "id",
        "reference",
        "objet",
        "montantEstime",
        "statut",
        "clientNom"
})
@Data
public class AppelDoffresDTO {

    private Long id;

    private String reference;

    private String objet;

    private Double montantEstime;

    private String statut;

    private String clientNom;
}
