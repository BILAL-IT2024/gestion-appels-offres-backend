package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarcheRepository extends JpaRepository<Marche, Long> {

    List<Marche> findByNumeroMarcheContainingIgnoreCase(
            String numeroMarche
    );

    long countByStatutIgnoreCase(String statut);

    // Statistiques par DAS
    long countByDas(Das das);

    @Query("""
        SELECT COALESCE(SUM(m.montantMarche), 0)
        FROM Marche m
        WHERE m.das = :das
        """)
    Double getMontantTotalByDas(Das das);

    @Query("""
            SELECT COALESCE(SUM(m.montantMarche), 0)
            FROM Marche m
            """)
    Double getMontantTotalMarches();

}