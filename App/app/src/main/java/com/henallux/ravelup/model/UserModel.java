package com.henallux.ravelup.model;

import java.util.Date;

public class UserModel {
    private String userName;
    private String password;
    private String passwordConfirm;
    private String email;
    private Date birthDate;
    private int idVille;

    public UserModel(String login, String motDePasse, String confirmeMotDePasse, String eMail, Date birthDate, int city) {
        setUserName(login);
        setPassword(motDePasse);
        setPasswordConfirm(confirmeMotDePasse);
        setEmail(eMail);
        setBirthDate(birthDate);
        setIdVille(city);
    }

    public UserModel() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if(userName == null){
            this.userName ="";
        }else
            this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password == null){
            this.password ="";
        }else
            this.password = password;
    }

    public String getPasswordConfirm() { return passwordConfirm; }

    public void setPasswordConfirm(String passwordConfirm) {
        if(passwordConfirm == null){
            this.passwordConfirm ="";
        }else
            this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email == null){
            this.email ="";
        }else
            this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
    }

    public int getIdVille() {
        return idVille;
    }

    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }
}
