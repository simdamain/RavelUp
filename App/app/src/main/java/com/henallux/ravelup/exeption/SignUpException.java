package com.henallux.ravelup.exeption;

public class SignUpException extends Exception {
    private String message;

    public SignUpException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}
