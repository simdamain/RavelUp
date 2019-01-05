package com.henallux.ravelup.exeptions;

public class PinException extends Exception {
    private String message;

    public PinException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}
