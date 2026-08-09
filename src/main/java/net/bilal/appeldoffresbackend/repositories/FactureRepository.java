package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
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
}