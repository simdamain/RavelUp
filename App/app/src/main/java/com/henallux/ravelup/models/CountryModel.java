package com.henallux.ravelup.models;

public class CountryModel {
    private long id;
    private String nom;

    public CountryModel(long id, String nom) {
        setId(id);
        setNom(nom);
    }

    public CountryModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
