package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    SELECT COALESCE(SUM(f.montantTTC), 0)
    FROM Facture f
    WHERE f.bonLivraison.id = :bonLivraisonId
    AND f.statut <> 'ANNULEE'
""")
    Double getTotalFactureByBonLivraisonId(
            @Param("bonLivraisonId") Long bonLivraisonId
    );

    long countByStatutIgnoreCase(String statut);

}