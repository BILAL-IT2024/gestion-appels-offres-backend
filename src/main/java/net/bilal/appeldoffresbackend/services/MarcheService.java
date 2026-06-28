package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.repositories.MarcheRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcheService {

    private final MarcheRepository marcheRepository;

    public List<Marche> getAllMarches() {
        return marcheRepository.findAll();
    }

    public Marche getMarcheById(Long id) {
        return marcheRepository.findById(id).orElse(null);
    }

    public Marche saveMarche(Marche marche) {
        return marcheRepository.save(marche);
    }

    public Marche updateMarche(Long id, Marche marche) {
        marche.setId(id);
        return marcheRepository.save(marche);
    }

    public List<Marche> rechercherMarches(String keyword) {
        return marcheRepository.findByNumeroMarcheContainingIgnoreCase(keyword);
    }

    public void deleteMarche(Long id) {
        marcheRepository.deleteById(id);
    }
}
