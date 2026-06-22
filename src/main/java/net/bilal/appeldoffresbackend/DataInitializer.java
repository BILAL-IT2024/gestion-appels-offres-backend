package net.bilal.appeldoffresbackend;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.*;
import net.bilal.appeldoffresbackend.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final AppelDoffresRepository appelDoffresRepository;
    private final MarcheRepository marcheRepository;
    private final CommandeRepository commandeRepository;
    private final PaiementRepository paiementRepository;

    @Override
    public void run(String... args) {

        System.out.println("INITIALISATION DES DONNEES...");

        if (clientRepository.count() > 0
                && appelDoffresRepository.count() > 0
                && marcheRepository.count() > 0
                && commandeRepository.count() > 0
                && paiementRepository.count() > 0) {
            return;
        }

        Client client = Client.builder()
                .raisonSociale("OCP SA")
                .adresse("Casablanca")
                .telephone("0600000000")
                .email("contact@ocp.ma")
                .typeClient("PUBLIC")
                .build();

        clientRepository.save(client);

        AppelDoffres ao = AppelDoffres.builder()
                .reference("AO-2026-001")
                .objet("Travaux électriques")
                .datePublication(LocalDate.now())
                .dateLimite(LocalDate.now().plusDays(30))
                .montantEstime(270000.0)
                .statut("ADJUGE")
                .client(client)
                .build();

        appelDoffresRepository.save(ao);

        Marche marche = Marche.builder()
                .numeroMarche("M-001")
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now().plusMonths(6))
                .montantMarche(250000.0)
                .tauxExecution(50.0)
                .statut("EN_COURS")
                .appelDoffres(ao)
                .build();

        marcheRepository.save(marche);

        Commande commande = Commande.builder()
                .numeroCommande("CMD-001")
                .dateCommande(LocalDate.now())
                .montantCommande(250000.0)
                .statut("EN_COURS")
                .marche(marche)
                .build();

        commandeRepository.save(commande);

        paiementRepository.save(
                Paiement.builder()
                        .datePaiement(LocalDate.of(2026,1,15))
                        .montantPaiement(15000.0)
                        .modePaiement("Virement")
                        .referencePaiement("P001")
                        .commande(commande)
                        .build());

        paiementRepository.save(
                Paiement.builder()
                        .datePaiement(LocalDate.of(2026,2,15))
                        .montantPaiement(20000.0)
                        .modePaiement("Virement")
                        .referencePaiement("P002")
                        .commande(commande)
                        .build());

        paiementRepository.save(
                Paiement.builder()
                        .datePaiement(LocalDate.of(2026,3,15))
                        .montantPaiement(18000.0)
                        .modePaiement("Virement")
                        .referencePaiement("P003")
                        .commande(commande)
                        .build());

        paiementRepository.save(
                Paiement.builder()
                        .datePaiement(LocalDate.of(2026,4,15))
                        .montantPaiement(25000.0)
                        .modePaiement("Virement")
                        .referencePaiement("P004")
                        .commande(commande)
                        .build());

        paiementRepository.save(
                Paiement.builder()
                        .datePaiement(LocalDate.of(2026,5,15))
                        .montantPaiement(30000.0)
                        .modePaiement("Virement")
                        .referencePaiement("P005")
                        .commande(commande)
                        .build());

        paiementRepository.save(
                Paiement.builder()
                        .datePaiement(LocalDate.of(2026,6,15))
                        .montantPaiement(35000.0)
                        .modePaiement("Virement")
                        .referencePaiement("P006")
                        .commande(commande)
                        .build());

        System.out.println("DONNEES DE TEST INSEREES !");
    }
}