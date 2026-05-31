package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Consultation;
import net.bilal.appeldoffresbackend.repositories.ConsultationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    public Consultation getConsultationById(Long id) {
        return consultationRepository.findById(id).orElse(null);
    }

    public Consultation saveConsultation(
            Consultation consultation) {

        return consultationRepository.save(consultation);
    }

    public Consultation updateConsultation(
            Long id,
            Consultation consultation) {

        consultation.setId(id);

        return consultationRepository.save(consultation);
    }

    public void deleteConsultation(Long id) {
        consultationRepository.deleteById(id);
    }
}
