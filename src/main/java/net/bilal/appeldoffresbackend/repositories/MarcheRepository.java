package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Marche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarcheRepository extends JpaRepository<Marche, Long> {

    List<Marche> findByNumeroMarcheContainingIgnoreCase(
            String numeroMarche
    );

    long countByStatutIgnoreCase(String statut);

    @Query("""
            SELECT COALESCE(SUM(m.montantMarche), 0)
            FROM Marche m
            """)
    Double getMontantTotalMarches();
}