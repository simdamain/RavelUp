package com.henallux.ravelup.model;

public class LoginModel {
    private String login;
    private String motDePasse;

    public LoginModel(String login, String motDePasse) {
        setLogin(login);
        setMotDePasse(motDePasse);
    }

    public LoginModel() {
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
}
