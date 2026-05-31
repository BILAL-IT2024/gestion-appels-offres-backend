package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.Client;
import net.bilal.appeldoffresbackend.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<Client> getClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PostMapping
    public Client saveClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }

    @PutMapping("/{id}")
    public Client updateClient(@PathVariable Long id,
                               @RequestBody Client client) {
        return clientService.updateClient(id, client);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}
