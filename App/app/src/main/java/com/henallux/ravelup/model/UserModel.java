package com.henallux.ravelup.model;

import java.util.Date;

public class UserModel {
    private String login;
    private String motDePasse;
    private String confirmeMotDePasse;
    private String eMail;
    private Date dateNaissance;

    public UserModel(String login, String motDePasse,String confirmeMotDePasse, String eMail, Date dateNaissance) {
        setLogin(login);
        setMotDePasse(motDePasse);
        setConfirmeMotDePasse(confirmeMotDePasse);
        seteMail(eMail);
        setDateNaissance(dateNaissance);
    }

    public UserModel() {
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if(login == null){
            this.login="";
        }else
            this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        if(motDePasse == null){
            this.motDePasse="";
        }else
            this.motDePasse = motDePasse;
    }

    public String getConfirmeMotDePasse() { return confirmeMotDePasse; }

    public void setConfirmeMotDePasse(String confirmeMotDePasse) {
        if(confirmeMotDePasse == null){
            this.confirmeMotDePasse="";
        }else
            this.confirmeMotDePasse = confirmeMotDePasse;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        if(eMail == null){
            this.eMail="";
        }else
            this.eMail = eMail;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        Date minAge= new Date(2006,01,01);
        if(dateNaissance.compareTo(minAge)>0)
            this.dateNaissance = dateNaissance;
    }
}
