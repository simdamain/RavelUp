package com.henallux.ravelup.exeption;

public class TrajetException extends Exception {
    private String message;

    public TrajetException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}
