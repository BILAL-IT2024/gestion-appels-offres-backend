package net.bilal.appeldoffresbackend.services;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Client;
import net.bilal.appeldoffresbackend.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client saveClient(Client client) {
        verifierClient(client);
        return clientRepository.save(client);
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client updateClient(Long id, Client client) {
        verifierClient(client);
        client.setId(id);
        return clientRepository.save(client);
    }

    public List<Client> rechercherClients(String keyword) {
        return clientRepository.findByRaisonSocialeContainingIgnoreCase(keyword);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    private void verifierClient(Client client) {

        if (client.getRaisonSociale() == null
                || client.getRaisonSociale().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La raison sociale est obligatoire"
            );
        }
    }

}
