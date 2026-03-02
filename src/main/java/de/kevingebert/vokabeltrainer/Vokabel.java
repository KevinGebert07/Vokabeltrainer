package de.kevingebert.vokabeltrainer;

import jakarta.persistence.*;

@Entity
@Table(name = "vokabel")
public class Vokabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vId;

    @Column(nullable = false)
    private String vonWort;

    @Column(nullable = false)
    private String zuWort;

    @ManyToOne(optional = false)
    @JoinColumn(name = "l_id", nullable = false)
    private Vokabelliste liste;

    public Vokabel() {
    }

    public Long getVId() {
        return vId;
    }

    public void setVId(Long vId) {
        this.vId = vId;
    }

    public String getVonWort() {
        return vonWort;
    }

    public void setVonWort(String vonWort) {
        this.vonWort = vonWort;
    }

    public String getZuWort() {
        return zuWort;
    }

    public void setZuWort(String zuWort) {
        this.zuWort = zuWort;
    }

    public Vokabelliste getListe() {
        return liste;
    }

    public void setListe(Vokabelliste liste) {
        this.liste = liste;
    }
}
