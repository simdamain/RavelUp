package com.henallux.ravelup.model;

public class CategoryModel {
    private Long id;
    private String libelle;

    public CategoryModel(Long id, String libelle) {
        setId(id);
        setLibelle(libelle);
    }

    public CategoryModel() {
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
