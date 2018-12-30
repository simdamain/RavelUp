package com.henallux.ravelup.model;

import java.util.ArrayList;

public class PointInteretTrajetModel {
    private Long idPointInteret;
    private ArrayList<JsonToTrajetModel> trajets;

    public PointInteretTrajetModel() {

    }

    public PointInteretTrajetModel(Long idPointInteret, ArrayList<JsonToTrajetModel> trajets) {
        setIdPointInteret(idPointInteret);
        setTrajets(trajets);
    }

    public Long getIdPointInteret() {
        return idPointInteret;
    }

    public void setIdPointInteret(Long idPointInteret) {
        this.idPointInteret = idPointInteret;
    }

    public ArrayList<JsonToTrajetModel> getTrajets() {
        return trajets;
    }

    public void setTrajets(ArrayList<JsonToTrajetModel> trajets) {
        this.trajets = trajets;
    }
}
