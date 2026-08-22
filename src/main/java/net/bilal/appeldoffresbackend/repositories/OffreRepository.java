package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Offre;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OffreRepository
        extends JpaRepository<Offre, Long> {

    List<Offre>
    findByReferenceContainingIgnoreCase(
            String reference
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