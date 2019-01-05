package com.henallux.ravelup.models;

import java.util.ArrayList;

public class TrajetModel {
    private Long id;
    private double nbKm;
    private String description;
    private Integer typeDeplacement; //switch  //1 cheval  2 velo  //3 pied
    private ArrayList<Long> points;

    public TrajetModel() {
    }

    public TrajetModel(Long id, ArrayList<Long> points) {
        this.id = id;
        this.points = points;
    }

    public TrajetModel(Long id, double nbKm, String description, Integer typeDeplacement, ArrayList<Long> points) {
        setId(id);
        setDescription(description);
        setNbKm(nbKm);
        setTypeDeplacement(typeDeplacement);
        setPoints(points);
    }

    public String descriptionRecyclerView(){
        String description = getDescription() + "\n Le parcours est de KM : " + getNbKm();
        switch (getTypeDeplacement()) {
            case 1:
                description+="\n Type de déplacement : à cheval";
                break;
            case 2:
                description+="\n Type de déplacement : à vélo";
                break;
            case 3:
                description+="\n Type de déplacement : à pied";
                break;
            default:
                description+="";
        }
        return description;
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

    public Integer getTypeDeplacement() {
        return typeDeplacement;
    }

    public void setTypeDeplacement(Integer typeDeplacement) {
        this.typeDeplacement = typeDeplacement;
    }

    public ArrayList<Long> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Long> points) {
        this.points = points;
    }
}
