package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.dtos.TopAppelOffreDTO;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppelDoffresRepository
        extends JpaRepository<AppelDoffres, Long> {

    long countByStatut(String statut);

    long countByDas(Das das);

    @Query("""
        SELECT COALESCE(SUM(a.montantEstime), 0)
        FROM AppelDoffres a
        WHERE a.das = :das
        """)
    Double getMontantTotalByDas(Das das);

    @Query("""
        SELECT a
        FROM AppelDoffres a
        WHERE a.dateLimite <= :dateLimite
        AND a.statut <> 'ADJUGE'
        """)
    List<AppelDoffres> getAppelsOffresUrgents(
            LocalDate dateLimite
    );

    List<AppelDoffres>
    findByReferenceContainingIgnoreCase(String reference);

    Page<AppelDoffres> findAll(Pageable pageable);

    @Query("""
        SELECT a
        FROM AppelDoffres a
        WHERE
        (:reference IS NULL
            OR LOWER(a.reference)
            LIKE LOWER(CONCAT('%', :reference, '%')))
        AND
        (:statut IS NULL
            OR a.statut = :statut)
        """)
    List<AppelDoffres> searchMultiCritere(
            String reference,
            String statut
    );

    @Query("""
        SELECT
            ao.reference as reference,
            ao.montantEstime as montant
        FROM AppelDoffres ao
        WHERE ao.reference IS NOT NULL
        AND ao.montantEstime IS NOT NULL
        ORDER BY ao.montantEstime DESC
        """)
    List<TopAppelOffreDTO> getTopAppelsOffres(
            Pageable pageable
    );


    // =========================================================
    // STATISTIQUES DASHBOARD PAR PERIODE
    // La date de référence d'un appel d'offres est datePublication
    // dateDebut incluse / dateFin exclue
    // =========================================================

    @Query("""
        SELECT COUNT(a)
        FROM AppelDoffres a
        WHERE a.datePublication >= :dateDebut
        AND a.datePublication < :dateFin
        """)
    long countByPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    @Query("""
        SELECT COUNT(a)
        FROM AppelDoffres a
        WHERE a.datePublication >= :dateDebut
        AND a.datePublication < :dateFin
        AND UPPER(a.statut) = UPPER(:statut)
        """)
    long countByStatutAndPeriode(
            @Param("statut") String statut,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    @Query("""
        SELECT COALESCE(SUM(a.montantEstime), 0)
        FROM AppelDoffres a
        WHERE a.datePublication >= :dateDebut
        AND a.datePublication < :dateFin
        """)
    Double getMontantTotalByPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}