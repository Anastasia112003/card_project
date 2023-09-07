package com.example.cardProject.model;

import lombok.Data;
import lombok.Getter;


@Getter

public record Card(String number, String validTill, String cvv, Amount amount) {
    public Card(String number, String validTill, String cvv, Amount amount) {
        this.number = number;
        this.validTill = validTill;
        this.cvv = cvv;
        this.amount = amount;
    }


}

