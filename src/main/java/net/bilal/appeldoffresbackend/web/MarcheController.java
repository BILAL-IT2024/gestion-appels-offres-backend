package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.services.MarcheService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marches")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MarcheController {

    private final MarcheService marcheService;

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

    @DeleteMapping("/{id}")
    public void deleteMarche(@PathVariable Long id) {
        marcheService.deleteMarche(id);
    }
}
