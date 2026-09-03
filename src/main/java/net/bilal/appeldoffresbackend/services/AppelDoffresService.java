package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppelDoffresService {

    private final AppelDoffresRepository appelDoffresRepository;

    public List<AppelDoffres> getAllAppelsOffres() {
        return appelDoffresRepository.findAll();
    }

    public AppelDoffres getAppelDoffresById(Long id) {
        return appelDoffresRepository.findById(id).orElse(null);
    }

    public AppelDoffres saveAppelDoffres(AppelDoffres appelDoffres) {

        verifierDatesAppelDoffres(appelDoffres);

        verifierMontantAppelDoffres(appelDoffres);

        return appelDoffresRepository.save(appelDoffres);
    }

    public AppelDoffres updateAppelDoffres(
            Long id,
            AppelDoffres appelDoffres
    ) {

        verifierDatesAppelDoffres(appelDoffres);

        verifierMontantAppelDoffres(appelDoffres);

        appelDoffres.setId(id);

        return appelDoffresRepository.save(appelDoffres);
    }

    public void deleteAppelDoffres(Long id) {
        appelDoffresRepository.deleteById(id);
    }

    private void verifierDatesAppelDoffres(AppelDoffres appelDoffres) {

        if (appelDoffres.getDatePublication() != null
                && appelDoffres.getDateLimite() != null
                && appelDoffres.getDateLimite()
                .isBefore(appelDoffres.getDatePublication())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La date limite ne peut pas être antérieure à la date de publication"
            );
        }
    }

    private void verifierMontantAppelDoffres(AppelDoffres appelDoffres) {

        if (appelDoffres.getMontantEstime() == null
                || appelDoffres.getMontantEstime() <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant estimé doit être supérieur à 0"
            );
        }
    }

}
