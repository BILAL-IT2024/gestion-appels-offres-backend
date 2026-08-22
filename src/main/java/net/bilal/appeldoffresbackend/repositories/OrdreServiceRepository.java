package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.OrdreService;
import net.bilal.appeldoffresbackend.enums.Das;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdreServiceRepository
        extends JpaRepository<OrdreService, Long> {

    List<OrdreService>
    findByNumeroOrdreContainingIgnoreCase(String keyword);

    List<OrdreService>
    findByMarcheId(Long marcheId);

    long countByMarcheDas(Das das);

}