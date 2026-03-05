package de.kevingebert.vokabeltrainer.dto;

public class AntwortDto {

    private Long vokabelId;
    private String deutschesWort;

    public Long getVokabelId() {
        return vokabelId;
    }

    public void setVokabelId(Long vokabelId) {
        this.vokabelId = vokabelId;
    }

    public String getDeutschesWort() {
        return deutschesWort;
    }

    public void setDeutschesWort(String deutschesWort) {
        this.deutschesWort = deutschesWort;
    }
}
