package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;
import net.bilal.appeldoffresbackend.entities.Marche;
import net.bilal.appeldoffresbackend.repositories.MarcheRepository;

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

}
