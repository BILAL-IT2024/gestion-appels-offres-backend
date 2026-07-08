package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Consultation;
import net.bilal.appeldoffresbackend.services.ConsultationService;
import org.springframework.web.bind.annotation.*;
import net.bilal.appeldoffresbackend.services.ExcelExportService;
import net.bilal.appeldoffresbackend.services.PdfExportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ConsultationController {

    private final ConsultationService consultationService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;

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

    @GetMapping("/search")
    public List<Consultation> searchConsultations(
            @RequestParam String keyword) {

        return consultationService.rechercherConsultations(keyword);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel() {

        InputStreamResource file =
                new InputStreamResource(
                        excelExportService.exportConsultationsToExcel()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=consultations.xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(file);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> exportPdf(
            @PathVariable Long id) {

        InputStreamResource file =
                new InputStreamResource(
                        pdfExportService.exportConsultationPdf(id)
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=consultation_" + id + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    @DeleteMapping("/{id}")
    public void deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
    }
}
