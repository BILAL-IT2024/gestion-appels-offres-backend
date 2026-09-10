package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.OrdreService;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdreServiceRepository
        extends JpaRepository<OrdreService, Long> {

    List<OrdreService>
    findByNumeroOrdreContainingIgnoreCase(String keyword);

    List<OrdreService>
    findByMarcheId(Long marcheId);

    long countByMarcheDas(Das das);

    @Query("""
    SELECT COUNT(o)
    FROM OrdreService o
    WHERE o.marche.das = :das
    AND o.dateOrdre >= :dateDebut
    AND o.dateOrdre < :dateFin
    """)
    long countByMarcheDasAndPeriode(
            @Param("das") Das das,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );

}