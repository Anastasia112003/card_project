package com.example.cardProject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Card {
    private String cardNumber;
    private String validTill;
    private String cvv;
    private Amount amount;
}

