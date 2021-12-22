package com.voluntarios.exception.custom;

public class EmailNotFoundException extends Exception{
    public EmailNotFoundException(String message) {
        super(message);
    }
}
