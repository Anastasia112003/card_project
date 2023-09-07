package com.example.cardProject.model;

import lombok.Getter;

@Getter
public record Request(String numberFrom,String validTill, String cvv, Amount amount, String numberTo) {
    public Request(String numberFrom, String validTill, String cvv, Amount amount, String numberTo) {
        this.numberFrom=numberFrom;
        this.validTill=validTill;
        this.cvv=cvv;
        this.amount=amount;
        this.numberTo=numberTo;
    }

}
