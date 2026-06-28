package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.services.MarcheService;
import net.bilal.appeldoffresbackend.services.PdfExportService;
import net.bilal.appeldoffresbackend.services.ExcelExportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/marches")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MarcheController {

    private final MarcheService marcheService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;

    @GetMapping
    public List<Marche> getAllMarches() {
        return marcheService.getAllMarches();
    }

    @GetMapping("/{id}")
    public Marche getMarche(@PathVariable Long id) {
        return marcheService.getMarcheById(id);
    }

    @PostMapping
    public Marche saveMarche(@RequestBody Marche marche) {
        return marcheService.saveMarche(marche);
    }

    @PutMapping("/{id}")
    public Marche updateMarche(
            @PathVariable Long id,
            @RequestBody Marche marche) {

        return marcheService.updateMarche(id, marche);
    }

    @GetMapping("/search")
    public List<Marche> searchMarches(@RequestParam String keyword) {
        return marcheService.rechercherMarches(keyword);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> exportPdf(
            @PathVariable Long id) {

        InputStreamResource file =
                new InputStreamResource(
                        pdfExportService.exportMarchePdf(id)
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=marche_" + id + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    @DeleteMapping("/{id}")
    public void deleteMarche(@PathVariable Long id) {
        marcheService.deleteMarche(id);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel() {

        InputStreamResource file =
                new InputStreamResource(
                        excelExportService.exportMarchesToExcel()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=marches.xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(file);
    }
}
