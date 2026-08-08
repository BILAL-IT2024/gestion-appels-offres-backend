package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.entities.OrdreService;
import net.bilal.appeldoffresbackend.repositories.MarcheRepository;
import net.bilal.appeldoffresbackend.repositories.OrdreServiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdreServiceService {

    private final OrdreServiceRepository ordreServiceRepository;
    private final MarcheRepository marcheRepository;


    public List<OrdreService> getAllOrdres() {
        return ordreServiceRepository.findAll();
    }


    public OrdreService getOrdreById(Long id) {

        return ordreServiceRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Ordre de service introuvable"
                        )
                );
    }


    public OrdreService saveOrdre(OrdreService ordre) {

        if (
                ordre.getMarche() == null
                        || ordre.getMarche().getId() == null
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Veuillez sélectionner un marché"
            );
        }

        Marche marche =
                marcheRepository
                        .findById(
                                ordre.getMarche().getId()
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Marché introuvable"
                                )
                        );

        ordre.setMarche(marche);

        return ordreServiceRepository.save(ordre);
    }


    public OrdreService updateOrdre(
            Long id,
            OrdreService ordre
    ) {

        OrdreService ordreExistant =
                getOrdreById(id);

        ordreExistant.setNumeroOrdre(
                ordre.getNumeroOrdre()
        );

        ordreExistant.setDateOrdre(
                ordre.getDateOrdre()
        );

        ordreExistant.setDateDebutExecution(
                ordre.getDateDebutExecution()
        );

        ordreExistant.setObjet(
                ordre.getObjet()
        );

        ordreExistant.setStatut(
                ordre.getStatut()
        );

        if (
                ordre.getMarche() != null
                        && ordre.getMarche().getId() != null
        ) {

            Marche marche =
                    marcheRepository
                            .findById(
                                    ordre.getMarche().getId()
                            )
                            .orElseThrow(() ->
                                    new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                                            "Marché introuvable"
                                    )
                            );

            ordreExistant.setMarche(marche);
        }

        return ordreServiceRepository.save(
                ordreExistant
        );
    }


    public List<OrdreService> rechercherOrdres(
            String keyword
    ) {

        return ordreServiceRepository
                .findByNumeroOrdreContainingIgnoreCase(
                        keyword
                );
    }


    public List<OrdreService> getOrdresByMarche(
            Long marcheId
    ) {

        return ordreServiceRepository
                .findByMarcheId(marcheId);
    }


    public void deleteOrdre(Long id) {

        if (!ordreServiceRepository.existsById(id)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ordre de service introuvable"
            );
        }

        ordreServiceRepository.deleteById(id);
    }
}