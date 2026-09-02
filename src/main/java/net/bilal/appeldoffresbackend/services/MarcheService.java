package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;

import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.entities.Offre;

import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;
import net.bilal.appeldoffresbackend.repositories.MarcheRepository;
import net.bilal.appeldoffresbackend.repositories.OffreRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcheService {

    private final MarcheRepository marcheRepository;

    private final AppelDoffresRepository
            appelDoffresRepository;

    private final OffreRepository offreRepository;


    public List<Marche> getAllMarches() {

        return marcheRepository.findAll();
    }


    public Marche getMarcheById(Long id) {

        return marcheRepository
                .findById(id)
                .orElse(null);
    }


    public Marche saveMarche(
            Marche marche
    ) {

        appliquerDonneesAppelOffres(
                marche
        );

        return marcheRepository.save(
                marche
        );
    }


    public Marche updateMarche(
            Long id,
            Marche marche
    ) {

        appliquerDonneesAppelOffres(
                marche
        );

        marche.setId(id);

        return marcheRepository.save(
                marche
        );
    }


    public List<Marche> rechercherMarches(
            String keyword
    ) {

        return marcheRepository
                .findByNumeroMarcheContainingIgnoreCase(
                        keyword
                );
    }


    public void deleteMarche(Long id) {

        marcheRepository.deleteById(id);
    }


    private void appliquerDonneesAppelOffres(
            Marche marche
    ) {

        // =============================================
        // VÉRIFICATION DE L'AO
        // =============================================

        if (
                marche.getAppelDoffres() == null
                        ||
                        marche.getAppelDoffres().getId()
                                == null
        ) {

            throw new IllegalArgumentException(
                    "Veuillez sélectionner un appel d'offres"
            );
        }


        Long appelDoffresId =
                marche
                        .getAppelDoffres()
                        .getId();


        AppelDoffres appelDoffres =
                appelDoffresRepository
                        .findById(
                                appelDoffresId
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Appel d'offres introuvable"
                                        )
                        );


        // =============================================
        // L'AO DOIT ÊTRE ADJUGÉ
        // =============================================

        if (
                appelDoffres.getStatut() == null
                        ||
                        !appelDoffres
                                .getStatut()
                                .equalsIgnoreCase(
                                        "ADJUGE"
                                )
        ) {

            throw new IllegalArgumentException(
                    "Seul un appel d'offres adjugé peut créer un marché"
            );
        }


        // =============================================
        // VÉRIFICATION DU DAS
        // =============================================

        if (
                appelDoffres.getDas() == null
        ) {

            throw new IllegalArgumentException(
                    "L'appel d'offres ne possède pas de DAS"
            );
        }


        // =============================================
        // CHERCHER L'OFFRE ACCEPTÉE
        // =============================================

        Offre offreAcceptee =
                offreRepository
                        .findFirstByAppelDoffres_IdAndStatutIgnoreCase(
                                appelDoffresId,
                                "ACCEPTEE"
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Aucune offre acceptée trouvée pour cet appel d'offres"
                                        )
                        );


        // =============================================
        // VÉRIFICATION DU MONTANT DE L'OFFRE
        // =============================================

        if (
                offreAcceptee.getMontantOffre()
                        == null
        ) {

            throw new IllegalArgumentException(
                    "L'offre acceptée ne possède pas de montant"
            );
        }


        // =============================================
        // DONNÉES DU MARCHÉ
        // =============================================

        // AO associé
        marche.setAppelDoffres(
                appelDoffres
        );


        // DAS provenant de l'AO
        marche.setDas(
                appelDoffres.getDas()
        );


        // Montant HT provenant de l'offre acceptée
        marche.setMontantMarche(
                offreAcceptee.getMontantOffre()
        );
    }

}