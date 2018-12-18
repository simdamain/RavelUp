package com.henallux.ravelup.model;

public class CityModel {
    private long id;
    private String libelle;
    private int codePostal;
    private CountryModel pays;

    public CityModel(long id, String libelle, int codePostal, CountryModel pays) {
        setId(id);
        setLibelle(libelle);
        setCodePostal(codePostal);
        setPays(pays);
    }

    public CityModel() {
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

    public CountryModel getPays() {
        return pays;
    }

    public void setPays(CountryModel pays) {
        this.pays = pays;
    }
}
