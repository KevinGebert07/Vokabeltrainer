package de.kevingebert.vokabeltrainer.controller;               // Paket, in dem der Controller liegt

import de.kevingebert.vokabeltrainer.model.Nutzer;              // Import des Nutzer-Entitätstypen
import de.kevingebert.vokabeltrainer.service.AuthService;       // Import des Anmeldungsservice
import jakarta.servlet.http.HttpSession;                        // Import der Sessionverwaltung eines eingeloggten Nutzers
import org.springframework.stereotype.Controller;               // Markiert diese Klasse als Spring MVC Controller
import org.springframework.ui.Model;                            // Transportiert Daten an das HTML-Template
import org.springframework.web.bind.annotation.*;               // Annotationen wie @GetMapping, @PostMapping

import java.util.Optional;

@Controller                                              // Klasse bearbeitet Web-Anfragen
public class AuthController {

    private final AuthService authService;                  // Assoziation zum Service der Login & Registrierung erledigt

    public AuthController(AuthService authService) {    // Konstruktor
        this.authService = authService;
    }

    @GetMapping("/login")                                // Wenn /login aufgerufen wird
    public String showLoginPage() {                         // Methode showLoginPage nutzen
        return "login";                                     // Thymeleaf-Template "login.html" zurückgeben
    }

    @PostMapping("/registrieren")                                        // POST-Anfrage auf /registrieren (Registrierung verarbeiten)
    public String handleRegister(@RequestParam String benutzername,         // Formularfeld "benutzername" auslesen
                                 @RequestParam String passwort,             // Formularfeld "passwort" auslesen
                                 Model model) {                             // Model für Erfolgs-/Fehlermeldungen

        boolean ok = authService.registrieren(benutzername, passwort);      // Service versucht, neuen Nutzer anzulegen
        if (!ok) {                                                          // Wenn Registrierung fehlgeschlagen ist
            model.addAttribute("fehlerRegistrierung",          // Meldung unter dem Registrier-Formular
                    "Benutzername ist bereits vergeben.");
        } else {                                                            // Wenn Registrierung erfolgreich war
            model.addAttribute("fehlerRegistrierung",          // Anzeigen der Erfolgsnachricht
                    "Registrierung erfolgreich. Bitte anmelden.");
        }
        return "login";                                                     // In beiden Fällen login.html anzeigen
    }

    @PostMapping("/anmelden")                                                // POST-Anfrage auf /anmelden (Login verarbeiten)
    public String handleLogin(@RequestParam String benutzername,                // Benutzername aus dem Formular
                              @RequestParam String passwort,                    // Passwort aus dem Formular
                              HttpSession session,                              // Session-Objekt um sich den Login zu merken
                                  Model model) {                                // Model für Fehlermeldung

        Optional<Nutzer> nutzerOpt = authService.einloggen(benutzername, passwort);         // Service prüft Login-Daten
        if (nutzerOpt.isEmpty()) {                                                          // Wenn kein Nutzer gefunden wurde
            model.addAttribute("fehlerLogin",                                  // Fehlermeldung für die Login-Seite
                    "Nutzername oder Passwort falsch.");
                return "login";                                                             // Login-Seite erneut anzeigen
        }

        Nutzer nutzer = nutzerOpt.get();                                           // Nutzer aus dem Optional holen
        session.setAttribute("nutzerId", nutzer.getNId());                      // Nutzer-ID in der Session speichern
        session.setAttribute("nutzerName", nutzer.getBenutzername());           // Benutzername in der Session speichern

        return "redirect:/index";                                                  // nach erfolgreichem Login auf Startseite umleiten
    }

    @GetMapping("/abgemeldet")                       // Wenn /abgemeldet aufgerufen wird
    public String logout(HttpSession session) {
            session.invalidate();                       // Nutzer abmelden: komplette Session löschen
        return "abgemeldet";                            // abgemeldet.html anzeigen
    }

}
