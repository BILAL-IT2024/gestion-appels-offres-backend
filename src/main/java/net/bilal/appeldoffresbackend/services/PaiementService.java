package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Paiement;
import net.bilal.appeldoffresbackend.repositories.PaiementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Paiement getPaiementById(Long id) {
        return paiementRepository.findById(id).orElse(null);
    }

    public Paiement savePaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    public Paiement updatePaiement(Long id,
                                   Paiement paiement) {

        paiement.setId(id);

        return paiementRepository.save(paiement);
    }

    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }
}
