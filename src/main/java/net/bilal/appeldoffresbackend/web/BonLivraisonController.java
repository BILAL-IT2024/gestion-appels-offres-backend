package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.BonLivraison;
import net.bilal.appeldoffresbackend.services.BonLivraisonService;
import net.bilal.appeldoffresbackend.services.ExcelExportService;
import net.bilal.appeldoffresbackend.services.PdfExportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/bons-livraison")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BonLivraisonController {

    private final BonLivraisonService bonLivraisonService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;


    @GetMapping
    public List<BonLivraison> getAllBonsLivraison() {

        return bonLivraisonService
                .getAllBonsLivraison();
    }


    @GetMapping("/{id}")
    public BonLivraison getBonLivraisonById(
            @PathVariable Long id
    ) {

        return bonLivraisonService
                .getBonLivraisonById(id);
    }


    @PostMapping
    public BonLivraison saveBonLivraison(
            @RequestBody BonLivraison bonLivraison
    ) {

        return bonLivraisonService
                .saveBonLivraison(bonLivraison);
    }


    @PutMapping("/{id}")
    public BonLivraison updateBonLivraison(
            @PathVariable Long id,
            @RequestBody BonLivraison bonLivraison
    ) {

        return bonLivraisonService
                .updateBonLivraison(
                        id,
                        bonLivraison
                );
    }


    @GetMapping("/search")
    public List<BonLivraison> rechercherBonsLivraison(
            @RequestParam String keyword
    ) {

        return bonLivraisonService
                .rechercherBonsLivraison(keyword);
    }


    @GetMapping("/commande/{commandeId}")
    public List<BonLivraison> getBonsByCommande(
            @PathVariable Long commandeId
    ) {

        return bonLivraisonService
                .getBonsByCommande(commandeId);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel() {

        InputStreamResource file =
                new InputStreamResource(
                        excelExportService
                                .exportBonsLivraisonToExcel()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=bons_livraison.xlsx"
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
                                .exportBonLivraisonPdf(id)
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=bon_livraison_"
                                + id
                                + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }


    @DeleteMapping("/{id}")
    public void deleteBonLivraison(
            @PathVariable Long id
    ) {

        bonLivraisonService
                .deleteBonLivraison(id);
    }
}