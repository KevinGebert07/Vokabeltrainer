package de.kevingebert.vokabeltrainer.dto;                  // Paket für DTO-Klassen (Daten-Transfer-Objekte)

public class AntwortDto {                                   // Einfache Klasse zum Transport der Lern-Antwort

    private Long vokabelId;                                 // Merkt sich, zu welcher Vokabel die Antwort gehört
    private String deutschesWort;                           // Das eingegebene deutsche Wort des Nutzers

    public Long getVokabelId() {                            // Getter: vokabelId auslesen
        return vokabelId;
    }

    public void setVokabelId(Long vokabelId) {              // Setter: vokabelId setzen
        this.vokabelId = vokabelId;
    }

    public String getDeutschesWort() {                      // Getter: deutschesWort auslesen
        return deutschesWort;
    }

    public void setDeutschesWort(String deutschesWort) {    // Setter: deutschesWort setzen
        this.deutschesWort = deutschesWort;
    }
}
