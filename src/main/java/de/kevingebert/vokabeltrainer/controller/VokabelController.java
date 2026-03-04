package de.kevingebert.vokabeltrainer.controller;

import de.kevingebert.vokabeltrainer.model.Nutzer;
import de.kevingebert.vokabeltrainer.model.Vokabel;
import de.kevingebert.vokabeltrainer.model.Vokabelliste;
import de.kevingebert.vokabeltrainer.repository.NutzerRepository;
import de.kevingebert.vokabeltrainer.repository.VokabellisteRepository;
import de.kevingebert.vokabeltrainer.repository.VokabelRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class VokabelController {

    private final NutzerRepository nutzerRepository;
    private final VokabellisteRepository vokabellisteRepository;
    private final VokabelRepository vokabelRepository;

    public VokabelController(NutzerRepository nutzerRepository,
                             VokabellisteRepository vokabellisteRepository,
                             VokabelRepository vokabelRepository) {
        this.nutzerRepository = nutzerRepository;
        this.vokabellisteRepository = vokabellisteRepository;
        this.vokabelRepository = vokabelRepository;
    }

    @GetMapping("/englisch")
    public String englisch(HttpSession session, Model model) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");
        if (nutzerId == null) {
            return "redirect:/login";
        }

        Optional<Nutzer> nutzerOpt = nutzerRepository.findById(nutzerId);
        if (nutzerOpt.isEmpty()) {
            return "redirect:/login";
        }
        Nutzer nutzer = nutzerOpt.get();

        Vokabelliste liste = vokabellisteRepository
                .findByNutzerAndVonSpracheAndZuSprache(nutzer, "EN", "DE")
                .orElseThrow(() -> new IllegalStateException("Englische Vokabelliste nicht gefunden"));

        List<Vokabel> vokabeln = vokabelRepository.findByListe(liste);

        model.addAttribute("liste", liste);
        model.addAttribute("vokabeln", vokabeln);
        model.addAttribute("neueVokabel", new Vokabel()); // für das Formular

        return "englisch";
    }

    @PostMapping("/englisch/vokabel-hinzufuegen")
    public String vokabelHinzufuegen(@ModelAttribute("neueVokabel") Vokabel neueVokabel,
                                     HttpSession session) {
        Long nutzerId = (Long) session.getAttribute("nutzerId");
        if (nutzerId == null) {
            return "redirect:/login";
        }

        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new IllegalStateException("Nutzer nicht gefunden"));

        Vokabelliste liste = vokabellisteRepository
                .findByNutzerAndVonSpracheAndZuSprache(nutzer, "EN", "DE")
                .orElseThrow(() -> new IllegalStateException("Englische Vokabelliste nicht gefunden"));

        neueVokabel.setListe(liste);
        vokabelRepository.save(neueVokabel);

        return "redirect:/englisch";
    }
}
