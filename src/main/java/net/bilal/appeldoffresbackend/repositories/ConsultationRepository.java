package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Consultation;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByReferenceContainingIgnoreCase(String reference);

    long countByStatutIgnoreCase(String statut);

    // Statistiques par DAS
    long countByDas(Das das);

    @Query("""
    SELECT COALESCE(SUM(c.montantPropose), 0)
    FROM Consultation c
    WHERE c.das = :das
    """)
    Double getMontantTotalByDas(Das das);

}
