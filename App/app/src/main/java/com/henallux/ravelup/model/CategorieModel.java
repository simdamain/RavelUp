package com.henallux.ravelup.model;

public class CategorieModel {
    private Long id;
    private String libelle;

    public CategorieModel(Long id, String libelle) {
        setId(id);
        setLibelle(libelle);
    }

    public CategorieModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
