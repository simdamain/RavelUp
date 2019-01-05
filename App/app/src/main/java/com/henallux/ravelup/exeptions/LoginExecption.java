package com.henallux.ravelup.exeptions;

public class LoginExecption extends Exception{
    private String message;

    public LoginExecption(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}
