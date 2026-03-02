package de.kevingebert.vokabeltrainer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/abgemeldet")
    public String abgemeldet() {
        return "abgemeldet";
    }

    @GetMapping("/impressum")
    public String impressum() {
        return "impressum";
    }

    @GetMapping("/englisch")
    public String englisch() {
        return "englisch";
    }

    @GetMapping("/russisch")
    public String russisch() {
        return "russisch";
    }
}


