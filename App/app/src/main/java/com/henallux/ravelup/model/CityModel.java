package com.henallux.ravelup.model;

public class CityModel {
    private int Id;
    private String Libelle;
    private int CodePostal;
    private CountryModel Pays;

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
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getLibelle() {
        return Libelle;
    }

    public void setLibelle(String libelle) {
        this.Libelle = libelle;
    }

    public int getCodePostal() {
        return CodePostal;
    }

    public void setCodePostal(int codePostal) {
        this.CodePostal = codePostal;
    }

    public CountryModel getPays() {
        return Pays;
    }

    public void setPays(CountryModel pays) {
        this.Pays = pays;
    }

    public String toSpinner(){return getLibelle()+" "+getCodePostal();}
}
