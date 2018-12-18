package com.henallux.ravelup.model;

import java.util.Date;

public class TokenReceived {

    private String token;
    private int code;
    private Date expirationDate;
    private String errorException;

    public TokenReceived() {
        this.errorException = "";
    }

    public TokenReceived(String token, int code, Date expirationDate, String errorException) {
        setToken(token);
        setCode(code);
        setExpirationDate(expirationDate);
        setErrorException(errorException);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getErrorException() {
        return errorException;
    }

    public void setErrorException(String errorException) {
        this.errorException = errorException;
    }
}

