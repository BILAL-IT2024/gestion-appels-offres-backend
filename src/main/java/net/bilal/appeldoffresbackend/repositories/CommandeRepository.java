package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByNumeroCommandeContainingIgnoreCase(String numeroCommande);
}
