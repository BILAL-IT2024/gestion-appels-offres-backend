package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.dtos.ChiffreAffaireMensuelDTO;
import net.bilal.appeldoffresbackend.dtos.TopClientDTO;
import net.bilal.appeldoffresbackend.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    @Query("""
            SELECT COALESCE(SUM(p.montantPaiement),0)
            FROM Paiement p
            """)
    Double getTotalChiffreAffaire();

    @Query("""
        SELECT
        MONTH(p.datePaiement) as mois,
        SUM(p.montantPaiement) as total

        FROM Paiement p

        GROUP BY MONTH(p.datePaiement)

        ORDER BY MONTH(p.datePaiement)
        """)
    List<ChiffreAffaireMensuelDTO> getChiffreAffaireMensuel();

    @Query(value = """
    SELECT
    c.raison_sociale as client,
    SUM(p.montant_paiement) as total

    FROM paiement p
    JOIN commande co ON p.commande_id = co.id
    JOIN marche m ON co.marche_id = m.id
    JOIN appel_doffres ao ON m.appel_doffres_id = ao.id
    JOIN client c ON ao.client_id = c.id

    GROUP BY c.raison_sociale

    ORDER BY total DESC

    LIMIT 5
    """,
            nativeQuery = true)
    List<TopClientDTO> getTopClients();
}
