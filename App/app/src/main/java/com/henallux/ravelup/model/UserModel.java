package com.henallux.ravelup.model;

import java.util.Date;

public class UserModel {
    private String UserName;
    private String Password;
    private String PasswordConfirm;
    private String Email;
    //TODO LOGIQUE
    private Date DateNaissance;
    private int city;

    public UserModel(String login, String motDePasse,String confirmeMotDePasse, String eMail, Date dateNaissance,int city) {
        setUserName(login);
        setPassword(motDePasse);
        setPasswordConfirm(confirmeMotDePasse);
        setEmail(eMail);
        setDateNaissance(dateNaissance);
        setCity(city);
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
        //Date minAge= new Date(2006,01,01);
        //if(dateNaissance.compareTo(minAge)>0)
            this.DateNaissance = dateNaissance;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }
}
