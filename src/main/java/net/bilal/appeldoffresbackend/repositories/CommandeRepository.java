package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Commande;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByNumeroCommandeContainingIgnoreCase(
            String numeroCommande
    );

    long countByStatutIgnoreCase(String statut);

    @Query("""
        SELECT COALESCE(SUM(c.montantCommande), 0)
        FROM Commande c
        """)
    Double getMontantTotalCommandes();

    @Query("""
        SELECT COALESCE(SUM(c.montantCommande), 0)
        FROM Commande c
        WHERE c.marche.id = :marcheId
        """)
    Double getTotalCommandesByMarcheId(
            @Param("marcheId") Long marcheId
    );

    @Query("""
        SELECT COALESCE(SUM(c.montantCommande), 0)
        FROM Commande c
        WHERE c.consultation.id = :consultationId
        AND c.statut <> 'ANNULEE'
        """)
    BigDecimal totalCommandesParConsultation(
            @Param("consultationId") Long consultationId
    );

    @Query("""
        SELECT COUNT(c)
        FROM Commande c
        LEFT JOIN c.marche m
        LEFT JOIN c.consultation cons
        WHERE COALESCE(m.das, cons.das) = :das
        """)
    long countByDas(
            @Param("das") Das das
    );

    @Query("""
        SELECT COALESCE(SUM(c.montantCommande), 0)
        FROM Commande c
        LEFT JOIN c.marche m
        LEFT JOIN c.consultation cons
        WHERE COALESCE(m.das, cons.das) = :das
        """)
    Double getMontantTotalByDas(
            @Param("das") Das das
    );


    // =========================================================
    // STATISTIQUES DASHBOARD PAR PERIODE
    // La date de référence d'une commande est dateCommande
    // dateDebut incluse / dateFin exclue
    // =========================================================

    @Query("""
        SELECT COUNT(c)
        FROM Commande c
        WHERE c.dateCommande >= :dateDebut
        AND c.dateCommande < :dateFin
        """)
    long countByPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    @Query("""
        SELECT COUNT(c)
        FROM Commande c
        WHERE c.dateCommande >= :dateDebut
        AND c.dateCommande < :dateFin
        AND UPPER(c.statut) = UPPER(:statut)
        """)
    long countByStatutAndPeriode(
            @Param("statut") String statut,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    @Query("""
        SELECT COALESCE(SUM(c.montantCommande), 0)
        FROM Commande c
        WHERE c.dateCommande >= :dateDebut
        AND c.dateCommande < :dateFin
        """)
    Double getMontantTotalByPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}