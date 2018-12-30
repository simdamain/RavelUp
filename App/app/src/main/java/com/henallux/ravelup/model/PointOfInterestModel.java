package com.henallux.ravelup.model;

import java.util.Collection;

public class PointOfInterestModel {
    private long id;
    private String nom;
    private String description;
    private Collection<ImageModel> imageList;
    private double longitude;
    private double latitude;
    private long categorieId;

    public PointOfInterestModel(long id, String nom, String description, Collection<ImageModel> imageList, double longitude,double latitude,long categorieId) {
        setId(id);
        setNom(nom);
        setDescription(description);
        setImageList(imageList);
        setLongitude(longitude);
        setLatitude(latitude);
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(long categorieId) {
        this.categorieId = categorieId;
    }
}
