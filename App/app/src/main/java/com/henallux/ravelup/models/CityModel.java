package com.henallux.ravelup.models;

public class CityModel {
    private int id;
    private String libelle;
    private int codePostal;
    private CountryModel pays;

    public CityModel(int id, String libelle, int codePostal, CountryModel pays) {
        setId(id);
        setLibelle(libelle);
        setCodePostal(codePostal);
        setPays(pays);
    }

    public CityModel(int id,String libelle, int codePostal) {
        setId(id);
        setLibelle(libelle);
        setCodePostal(codePostal);
    }

    public CityModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String toSpinner(){return getLibelle()+" "+getCodePostal();}
}
