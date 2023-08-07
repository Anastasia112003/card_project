package com.example.cardProject.service;

import com.example.cardProject.model.Amount;
import com.example.cardProject.model.Card;

public interface CardVerification {
    void  cardCheck (String cardNumberFrom, String cardNumberTo);
    void cvvCheck (String cvv);
    void dateCheck (String period);
    void balanceChange(int operationId);
    boolean balanceFromCardVerification(Card cardFrom, int commission, Amount amountOperation);
}
