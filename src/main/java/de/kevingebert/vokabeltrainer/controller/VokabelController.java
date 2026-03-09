package de.kevingebert.vokabeltrainer.controller;                           // Paket für alle Controller

import de.kevingebert.vokabeltrainer.dto.AntwortDto;                        // DTO (Daten-Transfer-Objekt) für die Antwort im Lernmodus
import de.kevingebert.vokabeltrainer.model.Nutzer;                          // Nutzer-Entitätstyp
import de.kevingebert.vokabeltrainer.model.Vokabel;                         // Vokabel-Entitätstyp
import de.kevingebert.vokabeltrainer.model.Vokabelliste;                    // Vokabelliste-Entitätstyp
import de.kevingebert.vokabeltrainer.repository.NutzerRepository;           // DB-Zugriff auf Nutzer
import de.kevingebert.vokabeltrainer.repository.VokabellisteRepository;     // DB-Zugriff auf Vokabellisten
import de.kevingebert.vokabeltrainer.repository.VokabelRepository;          // DB-Zugriff auf Vokabeln
import jakarta.servlet.http.HttpSession;                                    // Session um eingeloggten Nutzer zu merken
import org.springframework.stereotype.Controller;                           // Spring MVC Controller
import org.springframework.ui.Model;                                        // Daten an das HTML-Template übergeben
import org.springframework.web.bind.annotation.*;                           // @GetMapping, @PostMapping, @PathVariable, @ModelAttribute

import java.util.List;                                                      // Klasse List
import java.util.Optional;                                                  // Optional für „gefunden oder nicht“
import java.util.concurrent.ThreadLocalRandom;                              // Zufallszahlgenerator für zufällige Vokabel

@Controller                                                              // Markiert Klasse als Web-Controller
public class VokabelController {

    private final NutzerRepository nutzerRepository;                            // Repository für Nutzer
    private final VokabellisteRepository vokabellisteRepository;                // Repository für Vokabellisten
    private final VokabelRepository vokabelRepository;                          // Repository für Vokabeln

    public VokabelController(NutzerRepository nutzerRepository,                         // Konstruktor
                             VokabellisteRepository vokabellisteRepository,
                             VokabelRepository vokabelRepository) {
        this.nutzerRepository = nutzerRepository;                                       // initialisierung
        this.vokabellisteRepository = vokabellisteRepository;
        this.vokabelRepository = vokabelRepository;
    }


    /* --------------------- Normale Englisch-Seite --------------------- */

    @GetMapping("/englisch")                                                        // Wenn /englisch aufgerufen wird
    public String englisch(HttpSession session, Model model) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                    // Nutzer-ID aus der Session lesen
        if (nutzerId == null) {                                                        // Wenn niemand eingeloggt ist
            return "redirect:/login";                                                  // auf Login-Seite umleiten
        }

        Optional<Nutzer> nutzerOpt = nutzerRepository.findById(nutzerId);                   // Nutzer aus DB holen
        if (nutzerOpt.isEmpty()) {                                                          // Falls Nutzer nicht gefunden wird
            return "redirect:/login";                                                       // Zurück zum Login
        }
        Nutzer nutzer = nutzerOpt.get();                                                    // Nutzer-Objekt aus dem Optional holen

        Vokabelliste liste = vokabellisteRepository                                                             // passende Vokabelliste für Englisch finden
                .findByNutzerAndVonSpracheAndZuSprache(nutzer, "EN", "DE")
                .orElseThrow(() -> new IllegalStateException("Englische Vokabelliste nicht gefunden"));

        List<Vokabel> vokabeln = vokabelRepository.findByListe(liste);                                          // alle Vokabeln dieser Liste laden

        model.addAttribute("liste", liste);                                                        // Liste an Template übergeben
        model.addAttribute("vokabeln", vokabeln);                                                  // Vokabel-Liste an Template übergeben
        model.addAttribute("neueVokabel", new Vokabel());                                          // leeres Vokabel-Objekt für das Formular

        return "englisch";                                                                                      // Template englisch.html anzeigen
    }

    @PostMapping("/englisch/vokabel-hinzufuegen")                                                        // Wenn neue Vokabel gespeichert wird auf /englisch/vokabel-hinzufuegen
    public String vokabelHinzufuegen(@ModelAttribute("neueVokabel") Vokabel neueVokabel,                    // Vokabel aus Formular lesen
                                     HttpSession session) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                                        // prüfen ob Nutzer eingeloggt ist
        if (nutzerId == null) {
            return "redirect:/login";                                                                       // sonst zum Login
        }

        Nutzer nutzer = nutzerRepository.findById(nutzerId)                                                 // Nutzer laden
                .orElseThrow(() -> new IllegalStateException("Nutzer nicht gefunden"));                     // Wenn Nutzer nicht gefunden wurde Nachricht ausgeben

        Vokabelliste liste = vokabellisteRepository                                                         // passende Englisch-Liste finden
                .findByNutzerAndVonSpracheAndZuSprache(nutzer, "EN", "DE")
                .orElseThrow(() -> new IllegalStateException("Englische Vokabelliste nicht gefunden"));

        neueVokabel.setListe(liste);                                                                        // neue Vokabel mit dieser Liste verknüpfen
        vokabelRepository.save(neueVokabel);                                                                // neue Vokabel in DB speichern

        return "redirect:/englisch";                                                                        // zurück auf die Übersicht
    }

    @PostMapping("/englisch/vokabel-loeschen/{id}")                                                       // Wenn Vokabel gelöscht wird über /englisch/vokabel-loeschen/{id}
    public String vokabelLoeschen(@PathVariable("id") Long id,                                               // {id} aus der URL lesen
                                  HttpSession session) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                                          // nur wenn eingeloggt
        if (nutzerId == null) {
            return "redirect:/login";
        }

        vokabelRepository.deleteById(id);                                                                     // Vokabel mit dieser ID löschen
        return "redirect:/englisch";                                                                          // zurück zur Englisch-Übersicht
    }


    /* --------------------- Englisch lernen (GET) --------------------- */

    @GetMapping("/englisch/lernen")                                                                                  // GET /englisch/lernen -> Vokabel-Frage anzeigen
    public String englischLernen(HttpSession session, Model model) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                                                     // Login prüfen
        if (nutzerId == null) {
            return "redirect:/login";
        }

        Nutzer nutzer = nutzerRepository.findById(nutzerId)                                                             // Nutzer laden
                .orElseThrow(() -> new IllegalStateException("Nutzer nicht gefunden"));

        Vokabelliste liste = vokabellisteRepository                                                                     // Englisch-Liste des Nutzers holen
                .findByNutzerAndVonSpracheAndZuSprache(nutzer, "EN", "DE")
                .orElseThrow(() -> new IllegalStateException("Englische Vokabelliste nicht gefunden"));

        List<Vokabel> vokabeln = vokabelRepository.findByListe(liste);                                                  // alle Vokabeln holen
        if (vokabeln.isEmpty()) {                                                                                       // wenn keine Vokabeln vorhanden sind
            model.addAttribute("keineVokabeln", true);                                        // Flag setzen, damit Template Hinweis zeigt
            return "englischLernen";                                                                                    // Lernseite ohne Frage anzeigen
        }

        Vokabel zufaellig = zufaelligeVokabel(vokabeln);                                                                // zufällige Vokabel auswählen

        AntwortDto antwortDto = new AntwortDto();                                                                       // neues Antwort-DTO anlegen
        antwortDto.setVokabelId(zufaellig.getVId());                                                                    // ID der zufälligen Vokabel merken

        model.addAttribute("vokabel", zufaellig);                                                          // Frage (englisches Wort) ans Template
        model.addAttribute("antwort", antwortDto);                                                         // Antwort-Objekt für das Formular
        model.addAttribute("ergebnisAnzeigen", false);                                        // Noch kein Ergebnis anzeigen

        return "englischLernen";                                                                                        // Template englischLernen.html anzeigen
    }


    /* --------------------- Englisch lernen (POST) --------------------- */

    @PostMapping("/englisch/lernen")                                                                     // POST /englisch/lernen -> Antwort auswerten
    public String englischLernenPruefen(@ModelAttribute("antwort") AntwortDto antwort,                      // Formular-Daten
                                        HttpSession session,
                                        Model model) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                                         // Login prüfen
        if (nutzerId == null) {
            return "redirect:/login";
        }

        Optional<Vokabel> optVokabel = vokabelRepository.findById(antwort.getVokabelId());                  // Vokabel per ID holen
        if (optVokabel.isEmpty()) {                                                                         // falls nicht gefunden
            return "redirect:/englisch/lernen";                                                             // neue Frage laden
        }

        Vokabel vokabel = optVokabel.get();                                                                 // Vokabelobjekt bekommen

        String eingabe = antwort.getDeutschesWort() != null                                                 // Benutzereingabe holen, trimmen, klein schreiben
                ? antwort.getDeutschesWort().trim().toLowerCase()
                : "";

        String loesung = vokabel.getZuWort() != null                                                        // richtige Lösung holen, trimmen, klein schreiben
                ? vokabel.getZuWort().trim().toLowerCase()
                : "";

        boolean richtig = eingabe.equals(loesung);                                                          // Vergleichen ob Antwort stimmt

        model.addAttribute("vokabel", vokabel);                                                // Vokabel wieder ans Template geben
        model.addAttribute("antwort", antwort);                                                // eingegebene Antwort zurückgeben
        model.addAttribute("richtig", richtig);                                                // true/false für „richtig?“
        model.addAttribute("ergebnisAnzeigen", true);                             // Ergebnis-Bereich anzeigen

        return "englischLernen";                                                                            // gleiche Seite mit Ergebnis anzeigen
    }


    /* --------------------- Normale Russisch-Seite --------------------- */

    @GetMapping("/russisch")                                                                            // GET /russisch -> Russisch-Vokabelübersicht
    public String russisch(HttpSession session, Model model) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                                        // Login prüfen
        if (nutzerId == null) {
            return "redirect:/login";
        }

        Nutzer nutzer = nutzerRepository.findById(nutzerId)                                                // Nutzer laden
                .orElseThrow(() -> new IllegalStateException("Nutzer nicht gefunden"));

        Vokabelliste liste = vokabellisteRepository                                                        // russische Vokabelliste finden
                .findByNutzerAndVonSpracheAndZuSprache(nutzer, "RU", "DE")
                .orElseThrow(() -> new IllegalStateException("Russische Vokabelliste nicht gefunden"));

        List<Vokabel> vokabeln = vokabelRepository.findByListe(liste);                                      // alle russischen Vokabeln

        model.addAttribute("liste", liste);                                                    // Liste ans Template
        model.addAttribute("vokabeln", vokabeln);                                              // Vokabeln ans Template
        model.addAttribute("neueVokabel", new Vokabel());                                      // leere Vokabel für Formular

        return "russisch";                                                                                  // russisch.html anzeigen
    }

    @PostMapping("/russisch/vokabel-hinzufuegen")                                                         // POST /russisch/vokabel-hinzufuegen
    public String russischVokabelHinzufuegen(@ModelAttribute("neueVokabel") Vokabel neueVokabel,
                                             HttpSession session) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                                          // Login prüfen
        if (nutzerId == null) {
            return "redirect:/login";
        }

        Nutzer nutzer = nutzerRepository.findById(nutzerId)                                                   // Nutzer laden
                .orElseThrow(() -> new IllegalStateException("Nutzer nicht gefunden"));

        Vokabelliste liste = vokabellisteRepository                                                           // russische Liste holen
                .findByNutzerAndVonSpracheAndZuSprache(nutzer, "RU", "DE")
                .orElseThrow(() -> new IllegalStateException("Russische Vokabelliste nicht gefunden"));

        neueVokabel.setListe(liste);                                                                          // neue Vokabel mit Liste verknüpfen
        vokabelRepository.save(neueVokabel);                                                                  // Vokabel speichern

        return "redirect:/russisch";                                                                          // zurück zur Übersicht
    }

    @PostMapping("/russisch/vokabel-loeschen/{id}")                                                        // POST /russisch/vokabel-loeschen/{id}
    public String russischVokabelLoeschen(@PathVariable("id") Long id,
                                          HttpSession session) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                                           // Login prüfen
        if (nutzerId == null) {
            return "redirect:/login";
        }

        vokabelRepository.deleteById(id);                                                                     // Vokabel löschen
        return "redirect:/russisch";                                                                          // zurück zur Russisch-Übersicht
    }


    /* --------------------- Russisch lernen (GET) --------------------- */

    @GetMapping("/russisch/lernen")                                                                        // GET /russisch/lernen -> russische Frage anzeigen
    public String russischLernen(HttpSession session, Model model) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                                           // Login prüfen
        if (nutzerId == null) {
            return "redirect:/login";
        }

        Nutzer nutzer = nutzerRepository.findById(nutzerId)                                                    // Nutzer laden
                .orElseThrow(() -> new IllegalStateException("Nutzer nicht gefunden"));

        Vokabelliste liste = vokabellisteRepository                                                            // russische Liste holen
                .findByNutzerAndVonSpracheAndZuSprache(nutzer, "RU", "DE")
                .orElseThrow(() -> new IllegalStateException("Russische Vokabelliste nicht gefunden"));

        List<Vokabel> vokabeln = vokabelRepository.findByListe(liste);                                          // russische Vokabeln holen
        if (vokabeln.isEmpty()) {                                                                               // wenn keine vorhanden
            model.addAttribute("keineVokabeln", true);                                // Flag setzen
            return "russischLernen";                                                                            // Seite ohne Frage anzeigen
        }

        Vokabel zufaellig = zufaelligeVokabel(vokabeln);                                                        // zufällige russische Vokabel wählen

        AntwortDto antwortDto = new AntwortDto();                                                               // neues Antwort-DTO
        antwortDto.setVokabelId(zufaellig.getVId());                                                            // ID der Frage merken

        model.addAttribute("vokabel", zufaellig);                                                  // Vokabel ans Template
        model.addAttribute("antwort", antwortDto);                                                 // Antwortobjekt ans Template
        model.addAttribute("ergebnisAnzeigen", false);                                // noch kein Ergebnis anzeigen

        return "russischLernen";                                                                                // russischLernen.html anzeigen
    }


    /* --------------------- Russisch lernen (POST) --------------------- */

    @PostMapping("/russisch/lernen")                                                                    // POST /russisch/lernen -> Antwort bewerten
    public String russischLernenPruefen(@ModelAttribute("antwort") AntwortDto antwort,
                                        HttpSession session,
                                        Model model) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");                                        // Login prüfen
        if (nutzerId == null) {
            return "redirect:/login";
        }

        Optional<Vokabel> optVokabel = vokabelRepository.findById(antwort.getVokabelId());                 // Vokabel holen
        if (optVokabel.isEmpty()) {                                                                        // falls nicht gefunden
            return "redirect:/russisch/lernen";                                                            // neue Frage laden
        }

        Vokabel vokabel = optVokabel.get();                                                                // Vokabelobjekt

        String eingabe = antwort.getDeutschesWort() != null                                                // Eingabe normalisieren
                ? antwort.getDeutschesWort().trim().toLowerCase()
                : "";

        String loesung = vokabel.getZuWort() != null                                                       // Lösung normalisieren
                ? vokabel.getZuWort().trim().toLowerCase()
                : "";

        boolean richtig = eingabe.equals(loesung);                                                         // Prüfung ob Eingabe richtig oder falsch ist

        model.addAttribute("vokabel", vokabel);                                               // Vokabel ans Template
        model.addAttribute("antwort", antwort);                                               // eingegebene Antwort ans Template
        model.addAttribute("richtig", richtig);                                               // Ergebnis-Flag
        model.addAttribute("ergebnisAnzeigen", true);                            // Ergebnisbereich anzeigen

        return "russischLernen";                                                                           // gleiche Seite mit Ergebnis anzeigen
    }


    /* --------------------- Hilfsmethode --------------------- */

    private Vokabel zufaelligeVokabel(List<Vokabel> vokabeln) {                                             // wählt zufällig eine Vokabel aus der Liste
        int index = ThreadLocalRandom.current().nextInt(vokabeln.size());                                   // Zufallsindex von 0 bis size-1
        return vokabeln.get(index);                                                                         // Vokabel an dieser Stelle zurückgeben
    }
}
