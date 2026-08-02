package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.repositories.MarcheRepository;
import org.springframework.stereotype.Service;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcheService {

    private final MarcheRepository marcheRepository;
    private final AppelDoffresRepository appelDoffresRepository;

    public List<Marche> getAllMarches() {
        return marcheRepository.findAll();
    }

    public Marche getMarcheById(Long id) {
        return marcheRepository.findById(id).orElse(null);
    }

    public Marche saveMarche(Marche marche) {

        appliquerDonneesAppelOffres(marche);

        return marcheRepository.save(marche);
    }

    public Marche updateMarche(Long id, Marche marche) {

        appliquerDonneesAppelOffres(marche);

        marche.setId(id);

        return marcheRepository.save(marche);
    }

    public List<Marche> rechercherMarches(String keyword) {
        return marcheRepository.findByNumeroMarcheContainingIgnoreCase(keyword);
    }

    public void deleteMarche(Long id) {
        marcheRepository.deleteById(id);
    }

    private void appliquerDonneesAppelOffres(Marche marche) {

        if (
                marche.getAppelDoffres() == null
                        || marche.getAppelDoffres().getId() == null
        ) {
            throw new IllegalArgumentException(
                    "Veuillez sélectionner un appel d'offres"
            );
        }

        Long appelDoffresId =
                marche.getAppelDoffres().getId();

        AppelDoffres appelDoffres =
                appelDoffresRepository
                        .findById(appelDoffresId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Appel d'offres introuvable"
                                )
                        );

        if (
                appelDoffres.getStatut() == null
                        || !appelDoffres
                        .getStatut()
                        .equalsIgnoreCase("ADJUGE")
        ) {
            throw new IllegalArgumentException(
                    "Seul un appel d'offres adjugé peut créer un marché"
            );
        }

        if (appelDoffres.getDas() == null) {
            throw new IllegalArgumentException(
                    "L'appel d'offres ne possède pas de DAS"
            );
        }

        if (appelDoffres.getMontantEstime() == null) {
            throw new IllegalArgumentException(
                    "L'appel d'offres ne possède pas de montant"
            );
        }

        marche.setAppelDoffres(appelDoffres);
        marche.setDas(appelDoffres.getDas());
        marche.setMontantMarche(
                appelDoffres.getMontantEstime()
        );
    }

}
