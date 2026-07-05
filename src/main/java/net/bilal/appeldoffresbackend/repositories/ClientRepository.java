package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByRaisonSocialeContainingIgnoreCase(String raisonSociale);

}
