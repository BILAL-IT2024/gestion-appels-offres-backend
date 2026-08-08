package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Offre;
import net.bilal.appeldoffresbackend.services.OffreService;
import net.bilal.appeldoffresbackend.services.ExcelExportService;
import net.bilal.appeldoffresbackend.services.PdfExportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OffreController {

    private final OffreService offreService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;

    @GetMapping
    public List<Offre> getAllOffres() {
        return offreService.getAllOffres();
    }

    @GetMapping("/{id}")
    public Offre getOffreById(
            @PathVariable Long id
    ) {
        return offreService.getOffreById(id);
    }

    @PostMapping
    public Offre saveOffre(
            @RequestBody Offre offre
    ) {
        return offreService.saveOffre(offre);
    }

    @PutMapping("/{id}")
    public Offre updateOffre(
            @PathVariable Long id,
            @RequestBody Offre offre
    ) {
        return offreService.updateOffre(
                id,
                offre
        );
    }

    @GetMapping("/search")
    public List<Offre> rechercherOffres(
            @RequestParam String keyword
    ) {
        return offreService.rechercherOffres(
                keyword
        );
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel() {

        InputStreamResource file =
                new InputStreamResource(
                        excelExportService.exportOffresToExcel()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=offres.xlsx"
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
            @PathVariable Long id
    ) {

        InputStreamResource file =
                new InputStreamResource(
                        pdfExportService.exportOffrePdf(id)
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=offre_" + id + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    @DeleteMapping("/{id}")
    public void deleteOffre(
            @PathVariable Long id
    ) {
        offreService.deleteOffre(id);
    }
}