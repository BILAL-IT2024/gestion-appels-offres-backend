package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppelDoffresService {

    private final AppelDoffresRepository appelDoffresRepository;

    public List<AppelDoffres> getAllAppelsOffres() {
        return appelDoffresRepository.findAll();
    }

    public AppelDoffres getAppelDoffresById(Long id) {
        return appelDoffresRepository.findById(id).orElse(null);
    }

    public AppelDoffres saveAppelDoffres(AppelDoffres appelDoffres) {
        return appelDoffresRepository.save(appelDoffres);
    }

    public AppelDoffres updateAppelDoffres(Long id,
                                         AppelDoffres appelDoffres) {
        appelDoffres.setId(id);
        return appelDoffresRepository.save(appelDoffres);
    }

    public void deleteAppelDoffres(Long id) {
        appelDoffresRepository.deleteById(id);
    }

}
