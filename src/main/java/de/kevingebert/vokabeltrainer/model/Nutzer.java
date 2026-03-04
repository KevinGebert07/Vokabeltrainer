package de.kevingebert.vokabeltrainer.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nutzer")
public class Nutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nId;

    @Column(nullable = false, unique = true)
    private String benutzername;

    @Column(nullable = false)
    private String passwort;

    @OneToMany(mappedBy = "nutzer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vokabelliste> listen = new ArrayList<>();

    public Nutzer() {
    }

    public Long getNId() {
        return nId;
    }

    public void setNId(Long nId) {
        this.nId = nId;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public List<Vokabelliste> getListen() {
        return listen;
    }

    public void setListen(List<Vokabelliste> listen) {
        this.listen = listen;
    }

    public void addVokabelliste(Vokabelliste liste) {
        listen.add(liste);
        liste.setNutzer(this);
    }

}
