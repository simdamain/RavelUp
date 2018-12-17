package com.henallux.ravelup.model;

public class PaysModel {
    private long id;
    private String nom;

    public PaysModel(long id, String nom) {
        setId(id);
        setNom(nom);
    }

    public PaysModel() {
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
