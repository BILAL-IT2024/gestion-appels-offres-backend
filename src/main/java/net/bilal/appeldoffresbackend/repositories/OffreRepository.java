package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Offre;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OffreRepository
        extends JpaRepository<Offre, Long> {

    List<Offre>
    findByReferenceContainingIgnoreCase(
            String reference
    );

    // Récupérer l'offre acceptée d'un appel d'offres
    Optional<Offre>
    findFirstByAppelDoffres_IdAndStatutIgnoreCase(
            Long appelDoffresId,
            String statut
    );

    // Statistiques par DAS
    long countByDas(Das das);

    @Query("""
    SELECT COALESCE(SUM(o.montantOffre), 0)
    FROM Offre o
    WHERE o.das = :das
    """)
    Double getMontantTotalByDas(Das das);

}