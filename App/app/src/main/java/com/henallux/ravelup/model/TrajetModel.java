package com.henallux.ravelup.model;

import java.util.ArrayList;

public class TrajetModel {
    private Long id;
    private double nbKm;
    private String description;
    private Long TypeDeplacement; //switch
    private ArrayList<Long> points;

    public TrajetModel() {
    }

    public TrajetModel(Long id, ArrayList<Long> points) {
        this.id = id;
        this.points = points;
    }

    public TrajetModel(Long id, double nbKm, String description, Long typeDeplacement, ArrayList<Long> points) {
        setId(id);
        setDescription(description);
        setNbKm(nbKm);
        setTypeDeplacement(typeDeplacement);
        setPoints(points);
    }

    public String descriptionRecyclerView(){
        return getDescription()+" nombre de KM : "+getNbKm();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getNbKm() {
        return nbKm;
    }

    public void setNbKm(double nbKm) {
        this.nbKm = nbKm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTypeDeplacement() {
        return TypeDeplacement;
    }

    public void setTypeDeplacement(Long typeDeplacement) {
        TypeDeplacement = typeDeplacement;
    }

    public ArrayList<Long> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Long> points) {
        this.points = points;
    }
}
