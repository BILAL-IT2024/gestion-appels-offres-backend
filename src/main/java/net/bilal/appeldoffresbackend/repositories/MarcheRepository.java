package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Marche;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarcheRepository  extends JpaRepository<Marche, Long> {

    List<Marche> findByNumeroMarcheContainingIgnoreCase(String numeroMarche);
}
