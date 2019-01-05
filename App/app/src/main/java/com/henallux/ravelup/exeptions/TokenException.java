package com.henallux.ravelup.exeptions;

public class TokenException extends Exception {
    private String message;

    public TokenException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}
