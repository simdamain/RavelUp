package com.henallux.ravelup.model;

public class LoginModel {
    private String UserName;
    private String Password;

    public LoginModel(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    public LoginModel() {
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
}
