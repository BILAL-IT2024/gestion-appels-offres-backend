package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Consultation;
import net.bilal.appeldoffresbackend.services.ConsultationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping
    public List<Consultation> getAllConsultations() {
        return consultationService.getAllConsultations();
    }

    @GetMapping("/{id}")
    public Consultation getConsultation(@PathVariable Long id) {
        return consultationService.getConsultationById(id);
    }

    @PostMapping
    public Consultation saveConsultation(
            @RequestBody Consultation consultation) {

        return consultationService
                .saveConsultation(consultation);
    }

    @PutMapping("/{id}")
    public Consultation updateConsultation(
            @PathVariable Long id,
            @RequestBody Consultation consultation) {

        return consultationService
                .updateConsultation(id, consultation);
    }

    @DeleteMapping("/{id}")
    public void deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
    }
}
