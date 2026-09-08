package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MarcheRepository extends JpaRepository<Marche, Long> {

    List<Marche> findByNumeroMarcheContainingIgnoreCase(
            String numeroMarche
    );

    long countByStatutIgnoreCase(String statut);

    // Statistiques par DAS
    long countByDas(Das das);

    @Query("""
        SELECT COALESCE(SUM(m.montantMarche), 0)
        FROM Marche m
        WHERE m.das = :das
        """)
    Double getMontantTotalByDas(Das das);

    @Query("""
        SELECT COALESCE(SUM(m.montantMarche), 0)
        FROM Marche m
        """)
    Double getMontantTotalMarches();


    // =========================================================
    // STATISTIQUES DASHBOARD PAR PERIODE
    // La date de référence d'un marché est dateDebut
    // dateDebut incluse / dateFin exclue
    // =========================================================

    @Query("""
        SELECT COUNT(m)
        FROM Marche m
        WHERE m.dateDebut >= :dateDebut
        AND m.dateDebut < :dateFin
        """)
    long countByPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    @Query("""
        SELECT COUNT(m)
        FROM Marche m
        WHERE m.dateDebut >= :dateDebut
        AND m.dateDebut < :dateFin
        AND UPPER(m.statut) = UPPER(:statut)
        """)
    long countByStatutAndPeriode(
            @Param("statut") String statut,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    @Query("""
        SELECT COALESCE(SUM(m.montantMarche), 0)
        FROM Marche m
        WHERE m.dateDebut >= :dateDebut
        AND m.dateDebut < :dateFin
        """)
    Double getMontantTotalByPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}