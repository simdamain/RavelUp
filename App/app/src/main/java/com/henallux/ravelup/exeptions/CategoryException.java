package com.henallux.ravelup.exeptions;

public class CategoryException extends Exception {
    private String message;

    public CategoryException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return "erreur : "+message;
    }
}
