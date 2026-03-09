package de.kevingebert.vokabeltrainer.repository;                       // Paket für alle Repository-Schnittstellen

import de.kevingebert.vokabeltrainer.model.Vokabelliste;                // Entity-Klasse für Vokabellisten
import de.kevingebert.vokabeltrainer.model.Nutzer;                      // Nutzer-Entity, da in der Suche verwendet
import org.springframework.data.jpa.repository.JpaRepository;           // Basis-Interface für JPA-Repositories

import java.util.Optional;                                              // Optional für „Liste gefunden oder nicht“

public interface VokabellisteRepository extends JpaRepository<Vokabelliste, Long> {
    // Interface, Spring erzeugt die Implementierung automatisch
    // JpaRepository<Vokabelliste, Long>:
    //  - Entity-Typ: Vokabelliste
    //  - Primärschlüssel-Typ: Long

    Optional<Vokabelliste> findByNutzerAndVonSpracheAndZuSprache(
            Nutzer nutzer,       // Nutzer, dem die Liste gehört
            String vonSprache,   // Ausgangssprache, z.B. "EN" oder "RU"
            String zuSprache     // Zielsprache, z.B. "DE"
    );
    // Spring erzeugt automatisch eine Query:
    // SELECT ... FROM Vokabelliste
    // WHERE nutzer = ? AND vonSprache = ? AND zuSprache = ?
}
