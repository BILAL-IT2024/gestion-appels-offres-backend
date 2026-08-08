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
import net.bilal.appeldoffresbackend.entities.Paiement;
import net.bilal.appeldoffresbackend.repositories.PaiementRepository;
import net.bilal.appeldoffresbackend.entities.Client;
import net.bilal.appeldoffresbackend.repositories.ClientRepository;
import net.bilal.appeldoffresbackend.entities.Consultation;
import net.bilal.appeldoffresbackend.repositories.ConsultationRepository;
import net.bilal.appeldoffresbackend.entities.Offre;
import net.bilal.appeldoffresbackend.repositories.OffreRepository;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final AppelDoffresRepository appelDoffresRepository;
    private final MarcheRepository marcheRepository;
    private final CommandeRepository commandeRepository;
    private final PaiementRepository paiementRepository;
    private final ClientRepository clientRepository;
    private final ConsultationRepository consultationRepository;
    private final OffreRepository offreRepository;

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

    public ByteArrayInputStream exportPaiementPdf(Long id) {

        try {

            Paiement paiement =
                    paiementRepository
                            .findById(id)
                            .orElseThrow();

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);

            document.open();

            document.add(new Paragraph("FICHE PAIEMENT"));
            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Reference paiement : " + paiement.getReferencePaiement()));

            document.add(new Paragraph(
                    "Date paiement : " + paiement.getDatePaiement()));

            document.add(new Paragraph(
                    "Montant paiement : " + paiement.getMontantPaiement()));

            document.add(new Paragraph(
                    "Mode paiement : " + paiement.getModePaiement()));

            if (paiement.getCommande() != null) {
                document.add(new Paragraph(
                        "Commande : " + paiement.getCommande().getNumeroCommande()));
            }

            document.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erreur PDF Paiement", e);
        }
    }

    public ByteArrayInputStream exportClientPdf(Long id) {

        try {

            Client client =
                    clientRepository
                            .findById(id)
                            .orElseThrow();

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);

            document.open();

            document.add(new Paragraph("FICHE CLIENT"));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Raison sociale : " + client.getRaisonSociale()));
            document.add(new Paragraph("Adresse : " + client.getAdresse()));
            document.add(new Paragraph("Telephone : " + client.getTelephone()));
            document.add(new Paragraph("Email : " + client.getEmail()));
            document.add(new Paragraph("Type : " + client.getTypeClient()));

            document.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erreur PDF Client", e);
        }
    }

    public ByteArrayInputStream exportConsultationPdf(Long id) {

        try {

            Consultation consultation =
                    consultationRepository
                            .findById(id)
                            .orElseThrow();

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);

            document.open();

            document.add(new Paragraph("FICHE CONSULTATION"));
            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Reference : " + consultation.getReference()));

            document.add(new Paragraph(
                    "Objet : " + consultation.getObjet()));

            document.add(new Paragraph(
                    "Date reception : " + consultation.getDateReception()));

            document.add(new Paragraph(
                    "Montant propose : " + consultation.getMontantPropose()));

            document.add(new Paragraph(
                    "Statut : " + consultation.getStatut()));

            if (consultation.getClient() != null) {
                document.add(new Paragraph(
                        "Client : " + consultation.getClient().getRaisonSociale()));
            }

            document.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur PDF Consultation",
                    e
            );
        }
    }

    public ByteArrayInputStream exportOffrePdf(Long id) {

        try {

            Offre offre =
                    offreRepository
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
                    new Paragraph("FICHE OFFRE")
            );

            document.add(
                    new Paragraph(" ")
            );

            document.add(
                    new Paragraph(
                            "Reference : "
                                    + offre.getReference()
                    )
            );

            document.add(
                    new Paragraph(
                            "Date offre : "
                                    + offre.getDateOffre()
                    )
            );

            document.add(
                    new Paragraph(
                            "Montant offre : "
                                    + offre.getMontantOffre()
                                    + " DH"
                    )
            );

            document.add(
                    new Paragraph(
                            "Statut : "
                                    + offre.getStatut()
                    )
            );

            document.add(
                    new Paragraph(
                            "DAS : "
                                    + (
                                    offre.getDas() != null
                                            ? offre.getDas().toString()
                                            : ""
                            )
                    )
            );

            if (offre.getAppelDoffres() != null) {

                document.add(
                        new Paragraph(
                                "Source : Appel d'offres"
                        )
                );

                document.add(
                        new Paragraph(
                                "Appel d'offres : "
                                        + offre
                                        .getAppelDoffres()
                                        .getReference()
                        )
                );
            }

            if (offre.getConsultation() != null) {

                document.add(
                        new Paragraph(
                                "Source : Consultation"
                        )
                );

                document.add(
                        new Paragraph(
                                "Consultation : "
                                        + offre
                                        .getConsultation()
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
                    "Erreur PDF Offre",
                    e
            );
        }
    }

}