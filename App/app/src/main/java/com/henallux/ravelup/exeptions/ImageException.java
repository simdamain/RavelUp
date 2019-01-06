package com.henallux.ravelup.exeptions;

public class ImageException extends Exception{
    private String message;

    public ImageException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}