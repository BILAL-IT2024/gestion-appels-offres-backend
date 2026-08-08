package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Offre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffreRepository
        extends JpaRepository<Offre, Long> {

    List<Offre>
    findByReferenceContainingIgnoreCase(
            String reference
    );
}