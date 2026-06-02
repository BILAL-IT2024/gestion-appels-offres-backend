package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;

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
}
