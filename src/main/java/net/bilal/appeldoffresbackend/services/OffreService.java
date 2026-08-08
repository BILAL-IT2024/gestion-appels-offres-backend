package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.entities.Consultation;
import net.bilal.appeldoffresbackend.entities.Offre;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;
import net.bilal.appeldoffresbackend.repositories.ConsultationRepository;
import net.bilal.appeldoffresbackend.repositories.OffreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final OffreRepository offreRepository;
    private final AppelDoffresRepository appelDoffresRepository;
    private final ConsultationRepository consultationRepository;

    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    public Offre getOffreById(Long id) {
        return offreRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Offre introuvable"
                        )
                );
    }

    public Offre saveOffre(Offre offre) {

        appliquerSourceEtDas(offre);

        return offreRepository.save(offre);
    }

    public Offre updateOffre(
            Long id,
            Offre offre
    ) {

        if (!offreRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Offre introuvable"
            );
        }

        appliquerSourceEtDas(offre);

        offre.setId(id);

        return offreRepository.save(offre);
    }

    public List<Offre> rechercherOffres(
            String keyword
    ) {
        return offreRepository
                .findByReferenceContainingIgnoreCase(
                        keyword
                );
    }

    public void deleteOffre(Long id) {

        if (!offreRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Offre introuvable"
            );
        }

        offreRepository.deleteById(id);
    }

    private void appliquerSourceEtDas(
            Offre offre
    ) {

        boolean avecAO =
                offre.getAppelDoffres() != null
                        && offre.getAppelDoffres().getId() != null;

        boolean avecConsultation =
                offre.getConsultation() != null
                        && offre.getConsultation().getId() != null;

        if (avecAO && avecConsultation) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Une offre ne peut pas être liée à un appel d'offres et à une consultation en même temps"
            );
        }

        if (!avecAO && !avecConsultation) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'offre doit être liée à un appel d'offres ou à une consultation"
            );
        }

        if (offre.getMontantOffre() == null
                || offre.getMontantOffre() <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant de l'offre doit être supérieur à zéro"
            );
        }

        if (avecAO) {

            Long aoId =
                    offre.getAppelDoffres().getId();

            AppelDoffres ao =
                    appelDoffresRepository
                            .findById(aoId)
                            .orElseThrow(() ->
                                    new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                                            "Appel d'offres introuvable"
                                    )
                            );

            if (
                    ao.getStatut() != null
                            && ao.getStatut().equalsIgnoreCase("ANNULE")
            ) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Impossible de créer une offre pour un appel d'offres annulé"
                );
            }

            offre.setAppelDoffres(ao);
            offre.setConsultation(null);
            offre.setDas(ao.getDas());
        }

        if (avecConsultation) {

            Long consultationId =
                    offre.getConsultation().getId();

            Consultation consultation =
                    consultationRepository
                            .findById(consultationId)
                            .orElseThrow(() ->
                                    new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                                            "Consultation introuvable"
                                    )
                            );

            offre.setConsultation(consultation);
            offre.setAppelDoffres(null);
            offre.setDas(consultation.getDas());
        }
    }
}