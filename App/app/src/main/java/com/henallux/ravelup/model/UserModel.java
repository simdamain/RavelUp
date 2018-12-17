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
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getConfirmeMotDePasse() { return confirmeMotDePasse; }

    public void setConfirmeMotDePasse(String confirmeMotDePasse) { this.confirmeMotDePasse = confirmeMotDePasse; }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}
