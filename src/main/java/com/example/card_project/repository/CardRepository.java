package com.example.card_project.repository;

import com.example.card_project.model.Card;
import com.example.card_project.model.Request;

public interface CardRepository {
     void saveTransfer(int operationId, Request request);

     Request getTransferRequest(int operationId);

     int getId();

     void saveCode(int operationId, String confirmationCode);

     String getCode(int operationId);


     void saveCard(Card currentCardFrom , Card currentCardTo);

     Card getCard(String cardNumber);



}
