package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.dtos.ChiffreAffaireMensuelDTO;
import net.bilal.appeldoffresbackend.dtos.TopClientDTO;
import net.bilal.appeldoffresbackend.entities.Paiement;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    long countByStatutIgnoreCase(String statut);

    @Query("""
            SELECT COALESCE(SUM(p.montantPaiement),0)
            FROM Paiement p
            """)
    Double getTotalChiffreAffaire();

    @Query("""
            SELECT
            YEAR(p.datePaiement) as annee,
            MONTH(p.datePaiement) as mois,
            SUM(p.montantPaiement) as total

            FROM Paiement p

            WHERE UPPER(p.statut) = 'VALIDE'

            GROUP BY
            YEAR(p.datePaiement),
            MONTH(p.datePaiement)

            ORDER BY
            YEAR(p.datePaiement),
            MONTH(p.datePaiement)
              """)
    List<ChiffreAffaireMensuelDTO> getChiffreAffaireMensuel();


    @Query(value = """
    SELECT
        COALESCE(
            c_marche.raison_sociale,
            c_consultation.raison_sociale
        ) AS client,

        SUM(p.montant_paiement) AS total

    FROM paiement p

    JOIN commande co
        ON p.commande_id = co.id

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

    WHERE UPPER(p.statut) = 'VALIDE'

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
    List<TopClientDTO> getTopClients();


    List<Paiement> findByReferencePaiementContainingIgnoreCase(
            String referencePaiement
    );

    @Query("""
   SELECT COALESCE(SUM(p.montantPaiement), 0)
   FROM Paiement p
   WHERE p.facture.id = :factureId
   AND UPPER(p.statut) = 'VALIDE'
   """)
    Double getTotalPaiementsByFactureId(
            @Param("factureId") Long factureId
    );


    @Query("""
            SELECT COALESCE(SUM(p.montantPaiement), 0)
            FROM Paiement p
            WHERE p.facture IS NOT NULL
            AND UPPER(p.statut) = 'VALIDE'
            """)
    Double getTotalEncaisseFactures();


    @Query("""
       SELECT COALESCE(SUM(p.montantPaiement), 0)
       FROM Paiement p
       WHERE UPPER(p.statut) = 'VALIDE'
       """)
    Double getChiffreAffaireValide();

    List<Paiement> findByFactureId(Long factureId);

    // Statistiques par DAS
    @Query("""
    SELECT COALESCE(SUM(p.montantPaiement), 0)
    FROM Paiement p
    JOIN p.commande c
    LEFT JOIN c.marche m
    LEFT JOIN c.consultation cons
    WHERE UPPER(p.statut) = 'VALIDE'
    AND COALESCE(m.das, cons.das) = :das
    """)
    Double getMontantEncaisseByDas(
            @Param("das") Das das
    );

}
