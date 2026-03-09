package de.kevingebert.vokabeltrainer.repository;                       // Paket für alle Repository-Schnittstellen

import de.kevingebert.vokabeltrainer.model.Nutzer;                      // Nutzer-Entity, auf die dieses Repository arbeitet
import org.springframework.data.jpa.repository.JpaRepository;           // Basis-Interface für JPA-Repositories

import java.util.Optional;                                              // Optional für „Nutzer gefunden oder nicht“

public interface NutzerRepository extends JpaRepository<Nutzer, Long> {

    Optional<Nutzer> findByBenutzername(String benutzername);
    // Eigene Suchmethode:
    // Spring generiert automatisch eine Query "SELECT ... WHERE benutzername = ?"
    // Gibt Optional<Nutzer> zurück, weil es 0 oder 1 passenden Nutzer geben kann
}
