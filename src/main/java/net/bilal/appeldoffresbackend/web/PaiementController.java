package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Paiement;
import net.bilal.appeldoffresbackend.services.PaiementService;
import net.bilal.appeldoffresbackend.services.ExcelExportService;
import net.bilal.appeldoffresbackend.services.PdfExportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PaiementController {

    private final PaiementService paiementService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;

    @GetMapping
    public List<Paiement> getAllPaiements() {
        return paiementService.getAllPaiements();
    }

    @GetMapping("/{id}")
    public Paiement getPaiement(@PathVariable Long id) {
        return paiementService.getPaiementById(id);
    }

    @PostMapping
    public Paiement savePaiement(
            @RequestBody Paiement paiement) {

        return paiementService.savePaiement(paiement);
    }

    @PutMapping("/{id}")
    public Paiement updatePaiement(
            @PathVariable Long id,
            @RequestBody Paiement paiement) {

        return paiementService
                .updatePaiement(id, paiement);
    }

    @GetMapping("/search")
    public List<Paiement> searchPaiements(@RequestParam String keyword) {
        return paiementService.rechercherPaiements(keyword);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel() {

        InputStreamResource file =
                new InputStreamResource(
                        excelExportService.exportPaiementsToExcel()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=paiements.xlsx"
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
                        pdfExportService.exportPaiementPdf(id)
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=paiement_" + id + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    @GetMapping("/facture/{factureId}")
    public List<Paiement> getPaiementsByFacture(
            @PathVariable Long factureId
    ) {

        return paiementService
                .getPaiementsByFacture(factureId);
    }

    @GetMapping("/facture/{factureId}/resume")
    public Map<String, Double> getResumeFacture(
            @PathVariable Long factureId
    ) {

        return paiementService
                .getResumeFacture(factureId);
    }

    @DeleteMapping("/{id}")
    public void deletePaiement(@PathVariable Long id) {
        paiementService.deletePaiement(id);
    }

}
