package com.henallux.ravelup.model;

public class CategoryModel {
    private Long Id;
    private String Libelle;

    public CategoryModel(Long id, String libelle) {
        setId(id);
        setLibelle(libelle);
    }

    public CategoryModel(String libelle) {
        setLibelle(libelle);
    }

    public CategoryModel() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public String getLibelle() {
        return Libelle;
    }

    public void setLibelle(String libelle) {
        this.Libelle = libelle;
    }
}
