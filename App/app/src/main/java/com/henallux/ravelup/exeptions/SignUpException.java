package com.henallux.ravelup.exeptions;

public class SignUpException extends Exception {
    private String message;

    public SignUpException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}
