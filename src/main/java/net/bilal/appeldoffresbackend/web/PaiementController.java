package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Paiement;
import net.bilal.appeldoffresbackend.services.PaiementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PaiementController {

    private final PaiementService paiementService;

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

    @DeleteMapping("/{id}")
    public void deletePaiement(@PathVariable Long id) {
        paiementService.deletePaiement(id);
    }
}
