package de.kevingebert.vokabeltrainer.controller;

import de.kevingebert.vokabeltrainer.model.Nutzer;
import de.kevingebert.vokabeltrainer.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Zeigt immer die login.html mit beiden Formularen
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/registrieren")
    public String handleRegister(@RequestParam String benutzername,
                                 @RequestParam String passwort,
                                 Model model) {

        boolean ok = authService.registrieren(benutzername, passwort);
        if (!ok) {
            model.addAttribute("fehlerRegistrierung", "Benutzername ist bereits vergeben.");
        } else {
            model.addAttribute("fehlerRegistrierung", "Registrierung erfolgreich. Bitte anmelden.");
        }
        // gleiche Seite wieder anzeigen
        return "login";
    }

    @PostMapping("/anmelden")
    public String handleLogin(@RequestParam String benutzername,
                              @RequestParam String passwort,
                              HttpSession session,
                              Model model) {

        Optional<Nutzer> nutzerOpt = authService.einloggen(benutzername, passwort);
        if (nutzerOpt.isEmpty()) {
            model.addAttribute("fehlerLogin", "Nutzername oder Passwort falsch.");
            return "login";
        }

        Nutzer nutzer = nutzerOpt.get();
        session.setAttribute("nutzerId", nutzer.getNId());
        session.setAttribute("nutzerName", nutzer.getBenutzername());

        return "redirect:/index";  // nach erfolgreichem Login auf Startseite
    }

    @GetMapping("/abgemeldet")
    public String logout(HttpSession session) {
        session.invalidate();   // Nutzer abmelden (Session leeren)
        return "abgemeldet";    // abgemeldet.html anzeigen
    }

}
