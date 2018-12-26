package com.henallux.ravelup.model;

import java.util.ArrayList;

public class PinModel {
    private ArrayList<Integer> idCategories;
    private double longitude;
    private double latitude;
    private double rayon;


    public PinModel() {
    }

    public PinModel(ArrayList<Integer> idCategories, double longitude, double latitude, double rayon) {
        setIdCategories(idCategories);
        setLatitude(latitude);
        setLongitude(longitude);
        setRayon(rayon);
    }

    public ArrayList<Integer> getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(ArrayList<Integer> idCategories) {
        this.idCategories = idCategories;
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

    public double getRayon() {
        return rayon;
    }

    public void setRayon(double rayon) {
        this.rayon = rayon;
    }
}
