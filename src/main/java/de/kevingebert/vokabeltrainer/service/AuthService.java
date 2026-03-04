package de.kevingebert.vokabeltrainer.service;

import de.kevingebert.vokabeltrainer.model.Nutzer;
import de.kevingebert.vokabeltrainer.model.Vokabelliste;
import de.kevingebert.vokabeltrainer.repository.NutzerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final NutzerRepository nutzerRepository;

    public AuthService(NutzerRepository nutzerRepository) {
        this.nutzerRepository = nutzerRepository;
    }

    public boolean registrieren(String benutzername, String passwort) {
        if (nutzerRepository.findByBenutzername(benutzername).isPresent()) {
            return false; // Name schon vergeben
        }

        Nutzer nutzer = new Nutzer();
        nutzer.setBenutzername(benutzername);
        nutzer.setPasswort(passwort); // später: Passwort hashen

        // Standard-Vokabellisten anlegen
        Vokabelliste englisch = new Vokabelliste();
        englisch.setBezeichnung("Englisch");
        englisch.setVonSprache("EN");
        englisch.setZuSprache("DE");

        Vokabelliste russisch = new Vokabelliste();
        russisch.setBezeichnung("Russisch");
        russisch.setVonSprache("RU");
        russisch.setZuSprache("DE");

        // mit Nutzer verknüpfen (setzt auch liste.setNutzer(...))
        nutzer.addVokabelliste(englisch);
        nutzer.addVokabelliste(russisch);

        // speichert Nutzer + Listen dank CascadeType.ALL
        nutzerRepository.save(nutzer);

        return true;
    }


    public Optional<Nutzer> einloggen(String benutzername, String passwort) {
        return nutzerRepository.findByBenutzername(benutzername)
                .filter(n -> n.getPasswort().equals(passwort));
    }
}
