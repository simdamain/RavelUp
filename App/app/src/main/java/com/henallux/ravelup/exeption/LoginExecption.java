package com.henallux.ravelup.exeption;

public class LoginExecption extends Exception{
    private String message;

    public LoginExecption(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}
