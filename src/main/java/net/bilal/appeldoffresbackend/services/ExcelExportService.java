package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.repositories.MarcheRepository;
import net.bilal.appeldoffresbackend.entities.Commande;
import net.bilal.appeldoffresbackend.repositories.CommandeRepository;
import net.bilal.appeldoffresbackend.entities.Paiement;
import net.bilal.appeldoffresbackend.repositories.PaiementRepository;
import net.bilal.appeldoffresbackend.entities.Client;
import net.bilal.appeldoffresbackend.repositories.ClientRepository;
import net.bilal.appeldoffresbackend.entities.Consultation;
import net.bilal.appeldoffresbackend.repositories.ConsultationRepository;
import net.bilal.appeldoffresbackend.entities.Offre;
import net.bilal.appeldoffresbackend.repositories.OffreRepository;
import net.bilal.appeldoffresbackend.entities.OrdreService;
import net.bilal.appeldoffresbackend.repositories.OrdreServiceRepository;
import net.bilal.appeldoffresbackend.entities.BonLivraison;
import net.bilal.appeldoffresbackend.repositories.BonLivraisonRepository;
import net.bilal.appeldoffresbackend.entities.Facture;
import net.bilal.appeldoffresbackend.repositories.FactureRepository;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final AppelDoffresRepository appelDoffresRepository;
    private final MarcheRepository marcheRepository;
    private final CommandeRepository commandeRepository;
    private final PaiementRepository paiementRepository;
    private final ClientRepository clientRepository;
    private final ConsultationRepository consultationRepository;
    private final OffreRepository offreRepository;
    private final OrdreServiceRepository ordreServiceRepository;
    private final BonLivraisonRepository bonLivraisonRepository;
    private final FactureRepository factureRepository;

    public ByteArrayInputStream exportAOToExcel() {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet =
                    workbook.createSheet("AppelsOffres");

            Row header =
                    sheet.createRow(0);

            header.createCell(0)
                    .setCellValue("ID");

            header.createCell(1)
                    .setCellValue("Reference");

            header.createCell(2)
                    .setCellValue("Objet");

            header.createCell(3)
                    .setCellValue("Montant");

            header.createCell(4)
                    .setCellValue("Statut");

            List<AppelDoffres> list =
                    appelDoffresRepository.findAll();

            int rowNum = 1;

            for (AppelDoffres ao : list) {

                Row row = sheet.createRow(rowNum++);

                // ID
                row.createCell(0)
                        .setCellValue(
                                ao.getId() != null
                                        ? ao.getId()
                                        : 0
                        );

                // Référence
                row.createCell(1)
                        .setCellValue(
                                ao.getReference() != null
                                        ? ao.getReference()
                                        : ""
                        );

                // Objet
                row.createCell(2)
                        .setCellValue(
                                ao.getObjet() != null
                                        ? ao.getObjet()
                                        : ""
                        );

                // Montant
                row.createCell(3)
                        .setCellValue(
                                ao.getMontantEstime() != null
                                        ? ao.getMontantEstime()
                                        : 0
                        );

                // Statut
                row.createCell(4)
                        .setCellValue(
                                ao.getStatut() != null
                                        ? ao.getStatut()
                                        : ""
                        );
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            workbook.write(out);

            workbook.close();

            return new ByteArrayInputStream(
                    out.toByteArray()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur export Excel",
                    e
            );
        }
    }

    public ByteArrayInputStream exportMarchesToExcel() {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Marches");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Numero Marche");
            header.createCell(2).setCellValue("Date Debut");
            header.createCell(3).setCellValue("Date Fin");
            header.createCell(4).setCellValue("Montant Marche");
            header.createCell(5).setCellValue("Taux Execution");
            header.createCell(6).setCellValue("Statut");
            header.createCell(7).setCellValue("Appel Offre");

            List<Marche> list = marcheRepository.findAll();

            int rowNum = 1;

            for (Marche marche : list) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        marche.getId() != null ? marche.getId() : 0
                );

                row.createCell(1).setCellValue(
                        marche.getNumeroMarche() != null ? marche.getNumeroMarche() : ""
                );

                row.createCell(2).setCellValue(
                        marche.getDateDebut() != null ? marche.getDateDebut().toString() : ""
                );

                row.createCell(3).setCellValue(
                        marche.getDateFin() != null ? marche.getDateFin().toString() : ""
                );

                row.createCell(4).setCellValue(
                        marche.getMontantMarche() != null ? marche.getMontantMarche() : 0
                );

                row.createCell(5).setCellValue(
                        marche.getTauxExecution() != null ? marche.getTauxExecution() : 0
                );

                row.createCell(6).setCellValue(
                        marche.getStatut() != null ? marche.getStatut() : ""
                );

                row.createCell(7).setCellValue(
                        marche.getAppelDoffres() != null &&
                                marche.getAppelDoffres().getReference() != null
                                ? marche.getAppelDoffres().getReference()
                                : ""
                );
            }

            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur export Excel Marches",
                    e
            );
        }
    }

    public ByteArrayInputStream exportCommandesToExcel() {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Commandes");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Numero Commande");
            header.createCell(2).setCellValue("Date Commande");
            header.createCell(3).setCellValue("Montant");
            header.createCell(4).setCellValue("Statut");
            header.createCell(5).setCellValue("Origine");
            header.createCell(6).setCellValue("Marche / Consultation");

            List<Commande> list = commandeRepository.findAll();

            int rowNum = 1;

            for (Commande commande : list) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        commande.getId() != null ? commande.getId() : 0
                );

                row.createCell(1).setCellValue(
                        commande.getNumeroCommande() != null ? commande.getNumeroCommande() : ""
                );

                row.createCell(2).setCellValue(
                        commande.getDateCommande() != null ? commande.getDateCommande().toString() : ""
                );

                row.createCell(3).setCellValue(
                        commande.getMontantCommande() != null ? commande.getMontantCommande() : 0
                );

                row.createCell(4).setCellValue(
                        commande.getStatut() != null ? commande.getStatut() : ""
                );

                if (commande.getMarche() != null) {

                    row.createCell(5).setCellValue("MARCHE");

                    row.createCell(6).setCellValue(
                            commande.getMarche().getNumeroMarche() != null
                                    ? commande.getMarche().getNumeroMarche()
                                    : ""
                    );

                } else if (commande.getConsultation() != null) {

                    row.createCell(5).setCellValue("CONSULTATION");

                    row.createCell(6).setCellValue(
                            commande.getConsultation().getReference() != null
                                    ? commande.getConsultation().getReference()
                                    : ""
                    );

                } else {

                    row.createCell(5).setCellValue("");
                    row.createCell(6).setCellValue("");
                }
            }

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur export Excel Commandes",
                    e
            );
        }
    }

    public ByteArrayInputStream exportPaiementsToExcel() {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out =
                     new ByteArrayOutputStream()) {

            Sheet sheet =
                    workbook.createSheet("Paiements");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Reference");
            header.createCell(2).setCellValue("Date Paiement");
            header.createCell(3).setCellValue("Montant");
            header.createCell(4).setCellValue("Mode Paiement");
            header.createCell(5).setCellValue("Statut");
            header.createCell(6).setCellValue("Facture");
            header.createCell(7).setCellValue("Commande");

            List<Paiement> paiements =
                    paiementRepository.findAll();

            int rowIdx = 1;

            for (Paiement paiement : paiements) {

                Row row =
                        sheet.createRow(rowIdx++);

                // ID
                if (paiement.getId() != null) {
                    row.createCell(0)
                            .setCellValue(paiement.getId());
                }

                // Référence
                row.createCell(1).setCellValue(
                        paiement.getReferencePaiement() != null
                                ? paiement.getReferencePaiement()
                                : ""
                );

                // Date paiement
                row.createCell(2).setCellValue(
                        paiement.getDatePaiement() != null
                                ? paiement.getDatePaiement().toString()
                                : ""
                );

                // Montant
                if (paiement.getMontantPaiement() != null) {
                    row.createCell(3)
                            .setCellValue(
                                    paiement.getMontantPaiement()
                            );
                }

                // Mode paiement
                row.createCell(4).setCellValue(
                        paiement.getModePaiement() != null
                                ? paiement.getModePaiement()
                                : ""
                );

                // Statut
                row.createCell(5).setCellValue(
                        paiement.getStatut() != null
                                ? paiement.getStatut()
                                : ""
                );

                // Facture
                row.createCell(6).setCellValue(
                        paiement.getFacture() != null &&
                                paiement.getFacture()
                                        .getNumeroFacture() != null
                                ? paiement.getFacture()
                                .getNumeroFacture()
                                : ""
                );

                // Commande
                row.createCell(7).setCellValue(
                        paiement.getCommande() != null &&
                                paiement.getCommande()
                                        .getNumeroCommande() != null
                                ? paiement.getCommande()
                                .getNumeroCommande()
                                : ""
                );
            }

            // Ajustement automatique des 8 colonnes
            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return new ByteArrayInputStream(
                    out.toByteArray()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur export Excel Paiements",
                    e
            );
        }
    }

    public ByteArrayInputStream exportClientsToExcel() {

        try {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Clients");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Raison Sociale");
            header.createCell(2).setCellValue("Adresse");
            header.createCell(3).setCellValue("Téléphone");
            header.createCell(4).setCellValue("Email");
            header.createCell(5).setCellValue("Type");

            List<Client> list = clientRepository.findAll();

            int rowNum = 1;

            for (Client client : list) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        client.getId() != null ? client.getId() : 0
                );

                row.createCell(1).setCellValue(
                        client.getRaisonSociale() != null ? client.getRaisonSociale() : ""
                );

                row.createCell(2).setCellValue(
                        client.getAdresse() != null ? client.getAdresse() : ""
                );

                row.createCell(3).setCellValue(
                        client.getTelephone() != null ? client.getTelephone() : ""
                );

                row.createCell(4).setCellValue(
                        client.getEmail() != null ? client.getEmail() : ""
                );

                row.createCell(5).setCellValue(
                        client.getTypeClient() != null ? client.getTypeClient() : ""
                );
            }

            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erreur export Excel Clients", e);
        }
    }

    public ByteArrayInputStream exportConsultationsToExcel() {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Consultations");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Reference");
            header.createCell(2).setCellValue("Objet");
            header.createCell(3).setCellValue("Date Reception");
            header.createCell(4).setCellValue("Montant");
            header.createCell(5).setCellValue("Statut");
            header.createCell(6).setCellValue("Client");

            List<Consultation> list = consultationRepository.findAll();

            int rowNum = 1;

            for (Consultation consultation : list) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        consultation.getId() != null ? consultation.getId() : 0
                );

                row.createCell(1).setCellValue(
                        consultation.getReference() != null
                                ? consultation.getReference()
                                : ""
                );

                row.createCell(2).setCellValue(
                        consultation.getObjet() != null
                                ? consultation.getObjet()
                                : ""
                );

                row.createCell(3).setCellValue(
                        consultation.getDateReception() != null
                                ? consultation.getDateReception().toString()
                                : ""
                );

                row.createCell(4).setCellValue(
                        consultation.getMontantPropose() != null
                                ? consultation.getMontantPropose()
                                : 0
                );

                row.createCell(5).setCellValue(
                        consultation.getStatut() != null
                                ? consultation.getStatut()
                                : ""
                );

                row.createCell(6).setCellValue(
                        consultation.getClient() != null &&
                                consultation.getClient().getRaisonSociale() != null
                                ? consultation.getClient().getRaisonSociale()
                                : ""
                );
            }

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur export Excel Consultations",
                    e
            );
        }
    }

    public ByteArrayInputStream exportOffresToExcel() {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Offres");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Reference");
            header.createCell(2).setCellValue("Date Offre");
            header.createCell(3).setCellValue("Montant");
            header.createCell(4).setCellValue("Statut");
            header.createCell(5).setCellValue("DAS");
            header.createCell(6).setCellValue("Source");
            header.createCell(7).setCellValue("Reference Source");

            List<Offre> list =
                    offreRepository.findAll();

            int rowNum = 1;

            for (Offre offre : list) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        offre.getId() != null
                                ? offre.getId()
                                : 0
                );

                row.createCell(1).setCellValue(
                        offre.getReference() != null
                                ? offre.getReference()
                                : ""
                );

                row.createCell(2).setCellValue(
                        offre.getDateOffre() != null
                                ? offre.getDateOffre().toString()
                                : ""
                );

                row.createCell(3).setCellValue(
                        offre.getMontantOffre() != null
                                ? offre.getMontantOffre()
                                : 0
                );

                row.createCell(4).setCellValue(
                        offre.getStatut() != null
                                ? offre.getStatut()
                                : ""
                );

                row.createCell(5).setCellValue(
                        offre.getDas() != null
                                ? offre.getDas().toString()
                                : ""
                );

                if (offre.getAppelDoffres() != null) {

                    row.createCell(6)
                            .setCellValue("Appel d'offres");

                    row.createCell(7).setCellValue(
                            offre.getAppelDoffres().getReference() != null
                                    ? offre.getAppelDoffres().getReference()
                                    : ""
                    );

                } else if (offre.getConsultation() != null) {

                    row.createCell(6)
                            .setCellValue("Consultation");

                    row.createCell(7).setCellValue(
                            offre.getConsultation().getReference() != null
                                    ? offre.getConsultation().getReference()
                                    : ""
                    );

                } else {

                    row.createCell(6).setCellValue("");
                    row.createCell(7).setCellValue("");
                }
            }

            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(
                    out.toByteArray()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur export Excel Offres",
                    e
            );
        }
    }

    public ByteArrayInputStream exportOrdresServiceToExcel() {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet =
                    workbook.createSheet("OrdresService");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Numero Ordre");
            header.createCell(2).setCellValue("Date Ordre");
            header.createCell(3).setCellValue("Date Debut Execution");
            header.createCell(4).setCellValue("Objet");
            header.createCell(5).setCellValue("Statut");
            header.createCell(6).setCellValue("Marche");

            List<OrdreService> list =
                    ordreServiceRepository.findAll();

            int rowNum = 1;

            for (OrdreService ordre : list) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        ordre.getId() != null
                                ? ordre.getId()
                                : 0
                );

                row.createCell(1).setCellValue(
                        ordre.getNumeroOrdre() != null
                                ? ordre.getNumeroOrdre()
                                : ""
                );

                row.createCell(2).setCellValue(
                        ordre.getDateOrdre() != null
                                ? ordre.getDateOrdre().toString()
                                : ""
                );

                row.createCell(3).setCellValue(
                        ordre.getDateDebutExecution() != null
                                ? ordre.getDateDebutExecution().toString()
                                : ""
                );

                row.createCell(4).setCellValue(
                        ordre.getObjet() != null
                                ? ordre.getObjet()
                                : ""
                );

                row.createCell(5).setCellValue(
                        ordre.getStatut() != null
                                ? ordre.getStatut()
                                : ""
                );

                row.createCell(6).setCellValue(
                        ordre.getMarche() != null
                                && ordre.getMarche().getNumeroMarche() != null
                                ? ordre.getMarche().getNumeroMarche()
                                : ""
                );
            }

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(
                    out.toByteArray()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur export Excel Ordres de service",
                    e
            );
        }
    }

    public ByteArrayInputStream exportBonsLivraisonToExcel() {

        try {

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet =
                    workbook.createSheet("BonsLivraison");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Numero Bon");
            header.createCell(2).setCellValue("Date Livraison");
            header.createCell(3).setCellValue("Objet");
            header.createCell(4).setCellValue("Statut");
            header.createCell(5).setCellValue("Commande");
            header.createCell(6).setCellValue("Montant Livre");
            header.createCell(7).setCellValue("Marche / Consultation");

            List<BonLivraison> list =
                    bonLivraisonRepository.findAll();

            int rowNum = 1;

            for (BonLivraison bon : list) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        bon.getId() != null
                                ? bon.getId()
                                : 0
                );

                row.createCell(1).setCellValue(
                        bon.getNumeroBon() != null
                                ? bon.getNumeroBon()
                                : ""
                );

                row.createCell(2).setCellValue(
                        bon.getDateLivraison() != null
                                ? bon.getDateLivraison().toString()
                                : ""
                );

                row.createCell(3).setCellValue(
                        bon.getObjet() != null
                                ? bon.getObjet()
                                : ""
                );

                row.createCell(4).setCellValue(
                        bon.getStatut() != null
                                ? bon.getStatut()
                                : ""
                );

                row.createCell(5).setCellValue(
                        bon.getCommande() != null
                                && bon.getCommande().getNumeroCommande() != null
                                ? bon.getCommande().getNumeroCommande()
                                : ""
                );

                row.createCell(6).setCellValue(
                        bon.getMontantLivraison() != null
                                ? bon.getMontantLivraison()
                                : 0
                );

                if (
                        bon.getCommande() != null
                                && bon.getCommande().getMarche() != null
                ) {

                    row.createCell(7).setCellValue(
                            bon.getCommande()
                                    .getMarche()
                                    .getNumeroMarche() != null
                                    ? bon.getCommande()
                                    .getMarche()
                                    .getNumeroMarche()
                                    : ""
                    );

                } else if (
                        bon.getCommande() != null
                                && bon.getCommande().getConsultation() != null
                ) {

                    row.createCell(7).setCellValue(
                            bon.getCommande()
                                    .getConsultation()
                                    .getReference() != null
                                    ? bon.getCommande()
                                    .getConsultation()
                                    .getReference()
                                    : ""
                    );

                } else {

                    row.createCell(7).setCellValue("");
                }
            }

            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(
                    out.toByteArray()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur export Excel Bons de livraison",
                    e
            );
        }
    }

    public ByteArrayInputStream exportFacturesToExcel() {

        try {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Factures");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Numero facture");
            header.createCell(2).setCellValue("Date facture");
            header.createCell(3).setCellValue("Date echeance");
            header.createCell(4).setCellValue("Montant HT");
            header.createCell(5).setCellValue("TVA (%)");
            header.createCell(6).setCellValue("Montant TTC");
            header.createCell(7).setCellValue("Statut");
            header.createCell(8).setCellValue("Bon livraison");
            header.createCell(9).setCellValue("Montant livre");
            header.createCell(10).setCellValue("Commande");
            header.createCell(11).setCellValue("Marche / Consultation");

            List<Facture> factures =
                    factureRepository.findAll();

            int rowNum = 1;

            for (Facture facture : factures) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        facture.getId() != null
                                ? facture.getId()
                                : 0
                );

                row.createCell(1).setCellValue(
                        facture.getNumeroFacture() != null
                                ? facture.getNumeroFacture()
                                : ""
                );

                row.createCell(2).setCellValue(
                        facture.getDateFacture() != null
                                ? facture.getDateFacture().toString()
                                : ""
                );

                row.createCell(3).setCellValue(
                        facture.getDateEcheance() != null
                                ? facture.getDateEcheance().toString()
                                : ""
                );

                row.createCell(4).setCellValue(
                        facture.getMontantHT() != null
                                ? facture.getMontantHT()
                                : 0
                );

                row.createCell(5).setCellValue(
                        facture.getTva() != null
                                ? facture.getTva()
                                : 0
                );

                row.createCell(6).setCellValue(
                        facture.getMontantTTC() != null
                                ? facture.getMontantTTC()
                                : 0
                );

                row.createCell(7).setCellValue(
                        facture.getStatut() != null
                                ? facture.getStatut()
                                : ""
                );

                row.createCell(8).setCellValue(
                        facture.getBonLivraison() != null
                                && facture.getBonLivraison()
                                .getNumeroBon() != null
                                ? facture.getBonLivraison()
                                .getNumeroBon()
                                : ""
                );

                row.createCell(9).setCellValue(
                        facture.getBonLivraison() != null
                                && facture.getBonLivraison()
                                .getMontantLivraison() != null
                                ? facture.getBonLivraison()
                                .getMontantLivraison()
                                : 0
                );

                row.createCell(10).setCellValue(
                        facture.getBonLivraison() != null
                                && facture.getBonLivraison()
                                .getCommande() != null
                                && facture.getBonLivraison()
                                .getCommande()
                                .getNumeroCommande() != null
                                ? facture.getBonLivraison()
                                .getCommande()
                                .getNumeroCommande()
                                : ""
                );

                if (
                        facture.getBonLivraison() != null
                                && facture.getBonLivraison().getCommande() != null
                                && facture.getBonLivraison()
                                .getCommande()
                                .getMarche() != null
                ) {

                    row.createCell(11).setCellValue(
                            facture.getBonLivraison()
                                    .getCommande()
                                    .getMarche()
                                    .getNumeroMarche() != null
                                    ? facture.getBonLivraison()
                                    .getCommande()
                                    .getMarche()
                                    .getNumeroMarche()
                                    : ""
                    );

                } else if (
                        facture.getBonLivraison() != null
                                && facture.getBonLivraison().getCommande() != null
                                && facture.getBonLivraison()
                                .getCommande()
                                .getConsultation() != null
                ) {

                    row.createCell(11).setCellValue(
                            facture.getBonLivraison()
                                    .getCommande()
                                    .getConsultation()
                                    .getReference() != null
                                    ? facture.getBonLivraison()
                                    .getCommande()
                                    .getConsultation()
                                    .getReference()
                                    : ""
                    );

                } else {

                    row.createCell(11).setCellValue("");
                }
            }

            for (int i = 0; i < 12; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(
                    out.toByteArray()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur export Excel Factures",
                    e
            );
        }
    }

}
