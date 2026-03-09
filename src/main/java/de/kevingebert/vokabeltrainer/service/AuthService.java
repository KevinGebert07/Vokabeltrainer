package de.kevingebert.vokabeltrainer.service;                              // Paket für Service-Klassen (Geschäftslogik)

import de.kevingebert.vokabeltrainer.model.Nutzer;                          // Nutzer-Entity
import de.kevingebert.vokabeltrainer.model.Vokabelliste;                    // Vokabelliste-Entity
import de.kevingebert.vokabeltrainer.repository.NutzerRepository;           // Repository für Nutzer
import org.springframework.stereotype.Service;                              // Markiert Klasse als Service-Komponente

import java.util.Optional;                                                  // Optional für Rückgabe beim Login

@Service                                                                    // Sagt Spring: Diese Klasse ist ein Service
public class AuthService {

    private final NutzerRepository nutzerRepository;                        // Zugriff auf Nutzer in der Datenbank

    public AuthService(NutzerRepository nutzerRepository) {                 // Konstruktor
        this.nutzerRepository = nutzerRepository;
    }

    public boolean registrieren(String benutzername, String passwort) {

        if (nutzerRepository.findByBenutzername(benutzername).isPresent()) {                        // Prüfen, ob es den Benutzernamen schon gibt
            return false;                                                                           // Name schon vergeben -> Registrierung nicht erfolgreich
        }

        Nutzer nutzer = new Nutzer();                                                       // Neuen Nutzer anlegen
        nutzer.setBenutzername(benutzername);                                               // Benutzernamen setzen
        nutzer.setPasswort(passwort);                                                       // Passwort speichern (später: verschlüsseln)

        Vokabelliste englisch = new Vokabelliste();                                         // Liste für Englisch -> Deutsch
        englisch.setBezeichnung("Englisch");                                                // Anzeigename der Liste
        englisch.setVonSprache("EN");                                                       // Ausgangssprache Englisch
        englisch.setZuSprache("DE");                                                        // Zielsprache Deutsch

        Vokabelliste russisch = new Vokabelliste();                                         // Liste für Russisch -> Deutsch
        russisch.setBezeichnung("Russisch");                                                // Anzeigename der Liste
        russisch.setVonSprache("RU");                                                       // Ausgangssprache Russisch
        russisch.setZuSprache("DE");                                                        // Zielsprache Deutsch

        nutzer.addVokabelliste(englisch);
        nutzer.addVokabelliste(russisch);

        nutzerRepository.save(nutzer);

        return true;                                                                        // Registrierung erfolgreich
    }

    public Optional<Nutzer> einloggen(String benutzername, String passwort) {

        return nutzerRepository.findByBenutzername(benutzername)                            // Nutzer mit diesem Benutzernamen suchen
                .filter(n -> n.getPasswort().equals(passwort));                     // und nur zurückgeben, wenn das Passwort übereinstimmt

    }
}
