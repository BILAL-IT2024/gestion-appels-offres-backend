package net.bilal.appeldoffresbackend.services;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import net.bilal.appeldoffresbackend.repositories.AppelDoffresRepository;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvExportService {

    private final AppelDoffresRepository appelDoffresRepository;

    public String exportAOToCsv() {

        List<AppelDoffres> list =
                appelDoffresRepository.findAll();

        StringWriter stringWriter =
                new StringWriter();

        CSVWriter writer =
                new CSVWriter(stringWriter);

        String[] header = {
                "ID",
                "Reference",
                "Objet",
                "Montant",
                "Statut"
        };

        writer.writeNext(header);

        for (AppelDoffres ao : list) {

            String[] data = {
                    ao.getId().toString(),
                    ao.getReference(),
                    ao.getObjet(),
                    ao.getMontantEstime().toString(),
                    ao.getStatut()
            };

            writer.writeNext(data);
        }

        return stringWriter.toString();
    }
}
