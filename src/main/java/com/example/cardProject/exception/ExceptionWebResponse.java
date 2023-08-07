package com.example.cardProject.exception;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionWebResponse {

    private String errorMessage;
    private int id;

}