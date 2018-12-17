package com.henallux.ravelup.model;

import java.util.Collection;

public class PointOfInterestModel {
    private long id;
    private String nom;
    private String description;
    private Collection<ImageModel> imageList;
    private String QrCode;
    private long categorieId;

    public PointOfInterestModel(long id, String nom, String description, Collection<ImageModel> imageList, String qrCode, long categorieId) {
        setId(id);
        setNom(nom);
        setDescription(description);
        setImageList(imageList);
        setQrCode(qrCode);
        setCategorieId(categorieId);
    }

    public PointOfInterestModel() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<ImageModel> getImageList() {
        return imageList;
    }

    public void setImageList(Collection<ImageModel> imageList) {
        this.imageList = imageList;
    }

    public String getQrCode() {
        return QrCode;
    }

    public void setQrCode(String qrCode) {
        QrCode = qrCode;
    }

    public long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(long categorieId) {
        this.categorieId = categorieId;
    }
}
