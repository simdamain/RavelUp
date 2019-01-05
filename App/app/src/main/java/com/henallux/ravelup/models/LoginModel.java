package com.henallux.ravelup.models;

public class LoginModel {
    private String userName;
    private String password;

    public LoginModel(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    public LoginModel() {
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
}
