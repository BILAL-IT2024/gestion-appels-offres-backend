package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Commande;
import net.bilal.appeldoffresbackend.services.CommandeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommandeController {

    private final CommandeService commandeService;

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

    @DeleteMapping("/{id}")
    public void deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
    }
}
