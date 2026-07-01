package net.bilal.appeldoffresbackend.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.repositories.MarcheRepository;
import net.bilal.appeldoffresbackend.entities.Commande;
import net.bilal.appeldoffresbackend.repositories.CommandeRepository;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final AppelDoffresRepository appelDoffresRepository;
    private final MarcheRepository marcheRepository;
    private final CommandeRepository commandeRepository;

    public ByteArrayInputStream exportAOPdf(Long id) {

        try {

            AppelDoffres ao =
                    appelDoffresRepository
                            .findById(id)
                            .orElseThrow();

            Document document =
                    new Document();

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            PdfWriter.getInstance(
                    document,
                    out
            );

            document.open();

            document.add(
                    new Paragraph(
                            "FICHE APPEL D'OFFRES"
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            document.add(
                    new Paragraph(
                            "Reference : "
                                    + ao.getReference()
                    )
            );

            document.add(
                    new Paragraph(
                            "Objet : "
                                    + ao.getObjet()
                    )
            );

            document.add(
                    new Paragraph(
                            "Montant estime : "
                                    + ao.getMontantEstime()
                    )
            );

            document.add(
                    new Paragraph(
                            "Statut : "
                                    + ao.getStatut()
                    )
            );

            if (ao.getClient() != null) {

                document.add(
                        new Paragraph(
                                "Client : "
                                        + ao.getClient()
                                        .getRaisonSociale()
                        )
                );
            }

            document.close();

            return new ByteArrayInputStream(
                    out.toByteArray()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur PDF",
                    e
            );
        }
    }

    public ByteArrayInputStream exportMarchePdf(Long id) {

        try {

            Marche marche =
                    marcheRepository
                            .findById(id)
                            .orElseThrow();

            Document document =
                    new Document();

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            PdfWriter.getInstance(
                    document,
                    out
            );

            document.open();

            document.add(
                    new Paragraph(
                            "FICHE MARCHE"
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            document.add(
                    new Paragraph(
                            "Numero marche : "
                                    + marche.getNumeroMarche()
                    )
            );

            document.add(
                    new Paragraph(
                            "Date debut : "
                                    + marche.getDateDebut()
                    )
            );

            document.add(
                    new Paragraph(
                            "Date fin : "
                                    + marche.getDateFin()
                    )
            );

            document.add(
                    new Paragraph(
                            "Montant marche : "
                                    + marche.getMontantMarche()
                    )
            );

            document.add(
                    new Paragraph(
                            "Taux execution : "
                                    + marche.getTauxExecution()
                                    + " %"
                    )
            );

            document.add(
                    new Paragraph(
                            "Statut : "
                                    + marche.getStatut()
                    )
            );

            if (marche.getAppelDoffres() != null) {

                document.add(
                        new Paragraph(
                                "Appel d'offres : "
                                        + marche.getAppelDoffres()
                                        .getReference()
                        )
                );
            }

            document.close();

            return new ByteArrayInputStream(
                    out.toByteArray()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur PDF Marche",
                    e
            );
        }
    }

    public ByteArrayInputStream exportCommandePdf(Long id) {

        try {

            Commande commande =
                    commandeRepository
                            .findById(id)
                            .orElseThrow();

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);

            document.open();

            document.add(new Paragraph("FICHE COMMANDE"));
            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Numero commande : " + commande.getNumeroCommande()));

            document.add(new Paragraph(
                    "Date commande : " + commande.getDateCommande()));

            document.add(new Paragraph(
                    "Montant : " + commande.getMontantCommande()));

            document.add(new Paragraph(
                    "Statut : " + commande.getStatut()));

            if (commande.getMarche() != null) {
                document.add(new Paragraph(
                        "Marche : " + commande.getMarche().getNumeroMarche()));
            }

            document.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erreur PDF Commande", e);
        }
    }

}