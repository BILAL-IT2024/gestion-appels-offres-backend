package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.dtos.ChiffreAffaireMensuelDTO;
import net.bilal.appeldoffresbackend.dtos.TopClientDTO;
import net.bilal.appeldoffresbackend.entities.Facture;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FactureRepository
        extends JpaRepository<Facture, Long> {

    List<Facture>
    findByNumeroFactureContainingIgnoreCase(String keyword);

    List<Facture>
    findByBonLivraisonId(Long bonLivraisonId);

    List<Facture>
    findByStatutIgnoreCase(String statut);

    @Query("""
       SELECT COALESCE(SUM(f.montantTTC), 0)
       FROM Facture f
       """)
    Double getTotalFacture();

    @Query("""
        SELECT COALESCE(SUM(f.montantHT), 0)
        FROM Facture f
        WHERE f.bonLivraison.id = :bonLivraisonId
        AND f.statut <> 'ANNULEE'
        """)
    Double getTotalFactureByBonLivraisonId(
            @Param("bonLivraisonId") Long bonLivraisonId
    );

    long countByStatutIgnoreCase(String statut);

    // Statistiques par DAS
    @Query("""
        SELECT COALESCE(SUM(f.montantTTC), 0)
        FROM Facture f
        JOIN f.bonLivraison bl
        JOIN bl.commande c
        LEFT JOIN c.marche m
        LEFT JOIN c.consultation cons
        WHERE COALESCE(m.das, cons.das) = :das
        AND f.statut <> 'ANNULEE'
        """)
    Double getMontantTotalByDas(
            @Param("das") Das das
    );

    // Chiffre d'affaires total HT
    @Query("""
        SELECT COALESCE(SUM(f.montantHT), 0)
        FROM Facture f
        WHERE f.statut <> 'ANNULEE'
        """)
    Double getChiffreAffaireTotalHT();

    @Query("""
        SELECT
            YEAR(f.dateFacture) as annee,
            MONTH(f.dateFacture) as mois,
            SUM(f.montantHT) as total
        FROM Facture f
        WHERE f.statut <> 'ANNULEE'
        AND f.dateFacture IS NOT NULL
        GROUP BY
            YEAR(f.dateFacture),
            MONTH(f.dateFacture)
        ORDER BY
            YEAR(f.dateFacture),
            MONTH(f.dateFacture)
        """)
    List<ChiffreAffaireMensuelDTO> getChiffreAffaireMensuelHT();

    @Query("""
    SELECT
        YEAR(f.dateFacture) as annee,
        MONTH(f.dateFacture) as mois,
        SUM(f.montantHT) as total
    FROM Facture f
    WHERE UPPER(f.statut) <> 'ANNULEE'
    AND f.dateFacture IS NOT NULL
    AND YEAR(f.dateFacture) = :annee
    GROUP BY
        YEAR(f.dateFacture),
        MONTH(f.dateFacture)
    ORDER BY
        MONTH(f.dateFacture)
    """)
    List<ChiffreAffaireMensuelDTO> getChiffreAffaireMensuelHTByAnnee(
            Integer annee
    );

    @Query(value = """
        SELECT
            COALESCE(
                c_marche.raison_sociale,
                c_consultation.raison_sociale
            ) AS client,

            SUM(f.montantht) AS total

        FROM facture f

        JOIN bon_livraison bl
            ON f.bon_livraison_id = bl.id

        JOIN commande co
            ON bl.commande_id = co.id

        LEFT JOIN marche m
            ON co.marche_id = m.id

        LEFT JOIN appel_doffres ao
            ON m.appel_doffres_id = ao.id

        LEFT JOIN client c_marche
            ON ao.client_id = c_marche.id

        LEFT JOIN consultation cons
            ON co.consultation_id = cons.id

        LEFT JOIN client c_consultation
            ON cons.client_id = c_consultation.id

        WHERE UPPER(f.statut) <> 'ANNULEE'

        AND COALESCE(
            c_marche.id,
            c_consultation.id
        ) IS NOT NULL

        GROUP BY COALESCE(
            c_marche.raison_sociale,
            c_consultation.raison_sociale
        )

        ORDER BY total DESC

        LIMIT 5
        """,
            nativeQuery = true)
    List<TopClientDTO> getTopClientsByChiffreAffaireHT();

    @Query(value = """
    SELECT
        COALESCE(
            c_marche.raison_sociale,
            c_consultation.raison_sociale
        ) AS client,

        SUM(f.montantht) AS total

    FROM facture f

    JOIN bon_livraison bl
        ON f.bon_livraison_id = bl.id

    JOIN commande co
        ON bl.commande_id = co.id

    LEFT JOIN marche m
        ON co.marche_id = m.id

    LEFT JOIN appel_doffres ao
        ON m.appel_doffres_id = ao.id

    LEFT JOIN client c_marche
        ON ao.client_id = c_marche.id

    LEFT JOIN consultation cons
        ON co.consultation_id = cons.id

    LEFT JOIN client c_consultation
        ON cons.client_id = c_consultation.id

    WHERE UPPER(f.statut) <> 'ANNULEE'

    AND f.date_facture >= :dateDebut
    AND f.date_facture < :dateFin

    AND COALESCE(
        c_marche.id,
        c_consultation.id
    ) IS NOT NULL

    GROUP BY COALESCE(
        c_marche.raison_sociale,
        c_consultation.raison_sociale
    )

    ORDER BY total DESC

    LIMIT 5
    """,
            nativeQuery = true)

    List<TopClientDTO> getTopClientsByChiffreAffaireHTPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    // =========================================================
    // STATISTIQUES DASHBOARD PAR PERIODE
    // Date de référence : dateFacture
    // dateDebut incluse / dateFin exclue
    // =========================================================

    // Nombre total de factures de la période
    @Query("""
        SELECT COUNT(f)
        FROM Facture f
        WHERE f.dateFacture >= :dateDebut
        AND f.dateFacture < :dateFin
        """)
    long countByPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    // Nombre de factures selon le statut
    @Query("""
        SELECT COUNT(f)
        FROM Facture f
        WHERE f.dateFacture >= :dateDebut
        AND f.dateFacture < :dateFin
        AND UPPER(f.statut) = UPPER(:statut)
        """)
    long countByStatutAndPeriode(
            @Param("statut") String statut,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    // CA HT de la période
    // Les factures annulées ne participent pas au CA
    @Query("""
        SELECT COALESCE(SUM(f.montantHT), 0)
        FROM Facture f
        WHERE f.dateFacture >= :dateDebut
        AND f.dateFacture < :dateFin
        AND UPPER(f.statut) <> 'ANNULEE'
        """)
    Double getChiffreAffaireHTByPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );


    // Montant TTC facturé pendant la période
    // Les factures annulées sont exclues
    @Query("""
        SELECT COALESCE(SUM(f.montantTTC), 0)
        FROM Facture f
        WHERE f.dateFacture >= :dateDebut
        AND f.dateFacture < :dateFin
        AND UPPER(f.statut) <> 'ANNULEE'
        """)
    Double getMontantFactureTTCByPeriode(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}