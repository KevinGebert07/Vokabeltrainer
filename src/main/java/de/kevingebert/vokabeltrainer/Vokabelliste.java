package de.kevingebert.vokabeltrainer;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vokabelliste")
public class Vokabelliste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lId;

    @Column(nullable = false)
    private String bezeichnung;

    @Column(nullable = false)
    private String vonSprache;

    @Column(nullable = false)
    private String zuSprache;

    @ManyToOne(optional = false)
    @JoinColumn(name = "n_id", nullable = false)
    private Nutzer nutzer;

    @OneToMany(mappedBy = "liste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vokabel> vokabeln = new ArrayList<>();

    public Vokabelliste() {
    }

    public Long getLId() {
        return lId;
    }

    public void setLId(Long lId) {
        this.lId = lId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getVonSprache() {
        return vonSprache;
    }

    public void setVonSprache(String vonSprache) {
        this.vonSprache = vonSprache;
    }

    public String getZuSprache() {
        return zuSprache;
    }

    public void setZuSprache(String zuSprache) {
        this.zuSprache = zuSprache;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    public List<Vokabel> getVokabeln() {
        return vokabeln;
    }

    public void setVokabeln(List<Vokabel> vokabeln) {
        this.vokabeln = vokabeln;
    }
}
