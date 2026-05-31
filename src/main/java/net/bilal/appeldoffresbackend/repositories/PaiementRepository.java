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

    @Query("""
        SELECT
        ao.client.raisonSociale as client,
        SUM(p.montantPaiement) as total

        FROM Paiement p
        JOIN p.commande c
        JOIN c.marche m
        JOIN m.appelDoffres ao

        GROUP BY ao.client.raisonSociale

        ORDER BY total DESC
        """)
    List<TopClientDTO> getTopClients();
}
