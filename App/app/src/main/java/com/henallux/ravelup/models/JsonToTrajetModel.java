package com.henallux.ravelup.models;


import java.util.ArrayList;

public class JsonToTrajetModel {
    private long idTrajet;
    private ArrayList<Long> points;

    public JsonToTrajetModel() {
    }

    public JsonToTrajetModel(long idTrajet, ArrayList<Long> points) {
        setIdTrajet(idTrajet);
        setPoints(points);
    }

    public long getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(long idTrajet) {
        this.idTrajet = idTrajet;
    }

    public ArrayList<Long> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Long> points) {
        this.points = points;
    }
}
