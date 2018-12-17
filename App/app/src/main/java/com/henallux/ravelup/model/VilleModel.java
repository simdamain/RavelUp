package com.henallux.ravelup.model;

public class VilleModel {
    private long id;
    private String libelle;
    private int codePostal;
    private PaysModel pays;

    public VilleModel(long id, String libelle, int codePostal, PaysModel pays) {
        setId(id);
        setLibelle(libelle);
        setCodePostal(codePostal);
        setPays(pays);
    }

    public VilleModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public PaysModel getPays() {
        return pays;
    }

    public void setPays(PaysModel pays) {
        this.pays = pays;
    }
}
