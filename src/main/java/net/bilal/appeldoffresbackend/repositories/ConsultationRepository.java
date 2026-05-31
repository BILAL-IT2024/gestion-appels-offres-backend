package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
}
