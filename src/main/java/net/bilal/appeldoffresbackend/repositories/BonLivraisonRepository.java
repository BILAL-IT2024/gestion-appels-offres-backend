package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.BonLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonLivraisonRepository
        extends JpaRepository<BonLivraison, Long> {

    List<BonLivraison>
    findByNumeroBonContainingIgnoreCase(String keyword);

    List<BonLivraison>
    findByCommandeId(Long commandeId);
}