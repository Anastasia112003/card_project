package com.example.cardProject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private String numberFrom;
    private String validTill;
    private String cvv;
    private Amount amount;
    private String numberTo;
}
