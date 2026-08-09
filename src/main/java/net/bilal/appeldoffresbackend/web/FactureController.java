package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Facture;
import net.bilal.appeldoffresbackend.services.ExcelExportService;
import net.bilal.appeldoffresbackend.services.FactureService;
import net.bilal.appeldoffresbackend.services.PdfExportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FactureController {

    private final FactureService factureService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;


    @GetMapping
    public List<Facture> getAllFactures() {

        return factureService
                .getAllFactures();
    }


    @GetMapping("/{id}")
    public Facture getFactureById(
            @PathVariable Long id
    ) {

        return factureService
                .getFactureById(id);
    }


    @PostMapping
    public Facture saveFacture(
            @RequestBody Facture facture
    ) {

        return factureService
                .saveFacture(facture);
    }


    @PutMapping("/{id}")
    public Facture updateFacture(
            @PathVariable Long id,
            @RequestBody Facture facture
    ) {

        return factureService
                .updateFacture(
                        id,
                        facture
                );
    }


    @GetMapping("/search")
    public List<Facture> rechercherFactures(
            @RequestParam String keyword
    ) {

        return factureService
                .rechercherFactures(keyword);
    }


    @GetMapping("/bon-livraison/{bonLivraisonId}")
    public List<Facture> getFacturesByBonLivraison(
            @PathVariable Long bonLivraisonId
    ) {

        return factureService
                .getFacturesByBonLivraison(
                        bonLivraisonId
                );
    }


    @GetMapping("/statut/{statut}")
    public List<Facture> getFacturesByStatut(
            @PathVariable String statut
    ) {

        return factureService
                .getFacturesByStatut(statut);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel() {

        InputStreamResource file =
                new InputStreamResource(
                        excelExportService
                                .exportFacturesToExcel()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=factures.xlsx"
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
                                .exportFacturePdf(id)
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=facture_"
                                + id
                                + ".pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(file);
    }


    @DeleteMapping("/{id}")
    public void deleteFacture(
            @PathVariable Long id
    ) {

        factureService
                .deleteFacture(id);
    }
}