package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByReferenceContainingIgnoreCase(String reference);

}
