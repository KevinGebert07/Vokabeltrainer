package de.kevingebert.vokabeltrainer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/index")       // Wenn /index aufgerufen wird
    public String index() {        // Methode index aufrufen
        return "index";            // index.html anzeigen
    }

// -------- Vorher genutzte Routen - inzwischen in andere Controller ausgelagert --------

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }

//    @GetMapping("/abgemeldet")
//    public String abgemeldet() {
//        return "abgemeldet";
//    }

// ---------------------------------------------------------------------------------------

    @GetMapping("/impressum")    // Wenn /impressum aufgerufen wird
    public String impressum() {    // Methode impressum aufrufen
        return "impressum";        // impressum.html anzeigen
    }


// -------- Vorher genutzte Routen - inzwischen in andere Controller ausgelagert --------

//    @GetMapping("/englisch")
//    public String englisch() {
//        return "englisch";
//    }

//    @GetMapping("/russisch")
//    public String russisch() {
//        return "russisch";
//    }

// ---------------------------------------------------------------------------------------

    @GetMapping("/englischLernen")      // Wenn /englischLernen aufgerufen wird
    public String englischLernen() {       // Methode englischLernen aufrufen
        return "englischLernen";           // englischLernen.html anzeigen
    }

    @GetMapping("/russischLernen")      // Wenn /russischLernen aufgerufen wird
    public String russischLernen() {       // Methode russischLernen aufrufen
        return "russischLernen";           // russischLernen.html anzeigen
    }
}


