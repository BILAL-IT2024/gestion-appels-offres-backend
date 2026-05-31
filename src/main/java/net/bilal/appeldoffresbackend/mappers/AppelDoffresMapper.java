package net.bilal.appeldoffresbackend.mappers;

import net.bilal.appeldoffresbackend.dtos.AppelDoffresDTO;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import org.springframework.stereotype.Component;

@Component
public class AppelDoffresMapper {

    public AppelDoffresDTO fromAppelDoffres(AppelDoffres ao) {

        AppelDoffresDTO dto = new AppelDoffresDTO();

        dto.setId(ao.getId());
        dto.setReference(ao.getReference());
        dto.setObjet(ao.getObjet());
        dto.setMontantEstime(ao.getMontantEstime());
        dto.setStatut(ao.getStatut());

        if (ao.getClient() != null) {
            dto.setClientNom(ao.getClient().getRaisonSociale());
        }

        return dto;
    }
}
