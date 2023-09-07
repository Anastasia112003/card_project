package com.example.cardProject.model;

import lombok.*;

@Getter
public record Amount(int value, String currency) {
    public Amount(int value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public void setValue(int newBalanceCardTo) {
    }
}

