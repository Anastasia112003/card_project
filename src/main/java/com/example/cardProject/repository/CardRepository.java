package com.example.cardProject.repository;

import com.example.cardProject.model.Card;
import com.example.cardProject.model.Request;

public interface CardRepository {
     void saveTransfer(int operationId, Request request);

     Request getTransferRequest(int operationId);

     int getId();

     void saveCode(int operationId, String confirmationCode);

     String getCode(int operationId);


     void saveCard(Card currentCardFrom , Card currentCardTo);

     Card getCard(String cardNumber);



}
