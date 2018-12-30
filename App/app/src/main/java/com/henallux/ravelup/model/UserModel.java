package com.henallux.ravelup.model;

import java.util.Date;

public class UserModel {
    private String UserName;
    private String Password;
    private String PasswordConfirm;
    private String Email;
    //TODO LOGIQUE
    private Date DateNaissance;
    private int idVille;

    public UserModel(String login, String motDePasse,String confirmeMotDePasse, String eMail, Date dateNaissance,int city) {
        setUserName(login);
        setPassword(motDePasse);
        setPasswordConfirm(confirmeMotDePasse);
        setEmail(eMail);
        setDateNaissance(dateNaissance);
        setIdVille(city);
    }

    public UserModel() {
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        if(userName == null){
            this.UserName ="";
        }else
            this.UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        if(password == null){
            this.Password ="";
        }else
            this.Password = password;
    }

    public String getPasswordConfirm() { return PasswordConfirm; }

    public void setPasswordConfirm(String passwordConfirm) {
        if(passwordConfirm == null){
            this.PasswordConfirm ="";
        }else
            this.PasswordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        if(email == null){
            this.Email ="";
        }else
            this.Email = email;
    }

    public Date getDateNaissance() {
        return DateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
            this.DateNaissance = dateNaissance;
    }

    public int getIdVille() {
        return idVille;
    }

    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }
}
