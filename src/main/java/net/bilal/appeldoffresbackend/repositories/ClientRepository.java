package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
