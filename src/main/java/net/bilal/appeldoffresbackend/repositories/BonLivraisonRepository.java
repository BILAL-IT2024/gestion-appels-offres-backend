package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.BonLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonLivraisonRepository
        extends JpaRepository<BonLivraison, Long> {

    List<BonLivraison>
    findByNumeroBonContainingIgnoreCase(String keyword);

    List<BonLivraison>
    findByCommandeId(Long commandeId);

    @Query("""
    SELECT COALESCE(SUM(b.montantLivraison), 0)
    FROM BonLivraison b
    WHERE b.commande.id = :commandeId
    AND b.statut <> 'ANNULE'
""")
    Double getTotalLivreByCommandeId(
            @Param("commandeId") Long commandeId
    );

}