package net.bilal.appeldoffresbackend.repositories;

import net.bilal.appeldoffresbackend.entities.AppelDoffres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AppelDoffresRepository extends JpaRepository<AppelDoffres, Long> {

    long countBystatut(String statut);

    @Query("""
SELECT a FROM AppelDoffres a
WHERE a.dateLimite <= :dateLimite
AND a.statut <> 'ADJUGE'
""")
    List<AppelDoffres> getAppelsOffresUrgents(
            LocalDate dateLimite);

    List<AppelDoffres>
    findByReferenceContainingIgnoreCase(String reference);

    Page<AppelDoffres> findAll(Pageable pageable);

    @Query("""
SELECT a FROM AppelDoffres a

WHERE
(:reference IS NULL
OR LOWER(a.reference)
LIKE LOWER(CONCAT('%', :reference, '%')))

AND

(:statut IS NULL
OR a.statut = :statut)
""")
    List<AppelDoffres> searchMultiCritere(
            String reference,
            String statut
    );

}
