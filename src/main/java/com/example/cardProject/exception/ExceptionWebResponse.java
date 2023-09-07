package com.example.cardProject.exception;



public record ExceptionWebResponse(String errorMessage, int id) {

    public ExceptionWebResponse(String errorMessage, int id) {
        this.errorMessage = errorMessage;
        this.id = id;
    }
}