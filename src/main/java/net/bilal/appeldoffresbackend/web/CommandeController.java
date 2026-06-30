package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Commande;
import net.bilal.appeldoffresbackend.services.CommandeService;
import org.springframework.web.bind.annotation.*;
import net.bilal.appeldoffresbackend.services.ExcelExportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommandeController {

    private final CommandeService commandeService;
    private final ExcelExportService excelExportService;

    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeService.getAllCommandes();
    }

    @GetMapping("/{id}")
    public Commande getCommande(@PathVariable Long id) {
        return commandeService.getCommandeById(id);
    }

    @PostMapping
    public Commande saveCommande(
            @RequestBody Commande commande) {

        return commandeService.saveCommande(commande);
    }

    @PutMapping("/{id}")
    public Commande updateCommande(
            @PathVariable Long id,
            @RequestBody Commande commande) {

        return commandeService
                .updateCommande(id, commande);
    }

    @GetMapping("/search")
    public List<Commande> searchCommandes(@RequestParam String keyword) {
        return commandeService.rechercherCommandes(keyword);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel() {

        InputStreamResource file =
                new InputStreamResource(
                        excelExportService.exportCommandesToExcel()
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=commandes.xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(file);
    }

    @DeleteMapping("/{id}")
    public void deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
    }
}
