package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
}
