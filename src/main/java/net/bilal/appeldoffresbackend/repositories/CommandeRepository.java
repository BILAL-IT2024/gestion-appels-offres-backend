package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByNumeroCommandeContainingIgnoreCase(String numeroCommande);

    long countByStatutIgnoreCase(String statut);

    @Query("""
            SELECT COALESCE(SUM(c.montantCommande),0)
            FROM Commande c
            """)
    Double getMontantTotalCommandes();
}