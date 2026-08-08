package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.OrdreService;
import net.bilal.appeldoffresbackend.services.ExcelExportService;
import net.bilal.appeldoffresbackend.services.OrdreServiceService;
import net.bilal.appeldoffresbackend.services.PdfExportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/ordres-service")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrdreServiceController {

    private final OrdreServiceService ordreServiceService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;

    @GetMapping
    public List<OrdreService> getAllOrdres() {
        return ordreServiceService.getAllOrdres();
    }

    @GetMapping("/{id}")
    public OrdreService getOrdreById(
            @PathVariable Long id
    ) {
        return ordreServiceService.getOrdreById(id);
    }

    @PostMapping
    public OrdreService saveOrdre(
            @RequestBody OrdreService ordre
    ) {
        return ordreServiceService.saveOrdre(ordre);
    }

    @PutMapping("/{id}")
    public OrdreService updateOrdre(
            @PathVariable Long id,
            @RequestBody OrdreService ordre
    ) {
        return ordreServiceService.updateOrdre(
                id,
                ordre
        );
    }

    @GetMapping("/search")
    public List<OrdreService> rechercherOrdres(
            @RequestParam String keyword
    ) {
        return ordreServiceService
                .rechercherOrdres(keyword);
    }

    @GetMapping("/marche/{marcheId}")
    public List<OrdreService> getOrdresByMarche(
            @PathVariable Long marcheId
    ) {
        return ordreServiceService
                .getOrdresByMarche(marcheId);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel() {

        InputStreamResource file =
                new InputStreamResource(
                        excelExportService
                                .exportOrdresServiceToExcel()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ordres_service.xlsx"
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
                        pdfExportService
                                .exportOrdreServicePdf(id)
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=ordre_service_"
                                + id
                                + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    @DeleteMapping("/{id}")
    public void deleteOrdre(
            @PathVariable Long id
    ) {
        ordreServiceService.deleteOrdre(id);
    }
}