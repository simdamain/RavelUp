package com.henallux.ravelup.exeption;

public class CityException extends Exception {
    private String message;

    public CityException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}
