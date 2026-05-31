package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.dtos.AppelDoffresDTO;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.mappers.AppelDoffresMapper;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;
import net.bilal.appeldoffresbackend.services.AppelDoffresService;
import net.bilal.appeldoffresbackend.services.CsvExportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController
@RequestMapping("/api/appels-offres")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AppelDoffresController {

    private final AppelDoffresService appelDoffresService;
    private final AppelDoffresRepository appelDoffresRepository;
    private final CsvExportService csvExportService;
    private final AppelDoffresMapper appelDoffresMapper;

    /* @GetMapping
    public List<AppelDoffres> getAllAppelsOffres() {
        return appelDoffresService.getAllAppelsOffres();
    } */

    @GetMapping("/{id}")
    public AppelDoffres getAppelDoffres(@PathVariable Long id) {
        return appelDoffresService.getAppelDoffresById(id);
    }

    @PostMapping
    public AppelDoffres saveAppelDoffres(
            @RequestBody AppelDoffres appelDoffres) {

        return appelDoffresService.saveAppelDoffres(appelDoffres);
    }

    @PutMapping("/{id}")
    public AppelDoffres updateAppelDoffres(
            @PathVariable Long id,
            @RequestBody AppelDoffres appelDoffres) {

        return appelDoffresService
                .updateAppelDoffres(id, appelDoffres);
    }

    @DeleteMapping("/{id}")
    public void deleteAppelDoffres(@PathVariable Long id) {
        appelDoffresService.deleteAppelDoffres(id);
    }

    @GetMapping("/search")
    public List<AppelDoffres> searchAO(
            @RequestParam String keyword) {

        return appelDoffresRepository
                .findByReferenceContainingIgnoreCase(keyword);
    }

    @GetMapping("/pagination")
    public Page<AppelDoffres> getPaginatedAO(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        return appelDoffresRepository
                .findAll(pageable);
    }

    @GetMapping("/pagination-sort")
    public Page<AppelDoffres> getPaginatedAndSortedAO(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "datePublication")
            String sortBy
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortBy).descending()
                );

        return appelDoffresRepository
                .findAll(pageable);
    }

    @GetMapping("/search-multi")
    public List<AppelDoffres> searchMulti(

            @RequestParam(required = false)
            String reference,

            @RequestParam(required = false)
            String statut
    ) {

        return appelDoffresRepository
                .searchMultiCritere(
                        reference,
                        statut
                );
    }

    @GetMapping("/export/csv")
    public String exportCsv() {

        return csvExportService.exportAOToCsv();
    }

    @GetMapping
    public List<AppelDoffresDTO> getAllAO() {

        return appelDoffresRepository.findAll()
                .stream()
                .map(appelDoffresMapper::fromAppelDoffres)
                .toList();
    }
}
