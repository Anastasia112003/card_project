package com.example.card_project.service;

import com.example.card_project.model.OperationRequest;
import com.example.card_project.model.Request;
import com.example.card_project.model.Response;

public interface CardService {
    Response transfer(Request transferRequest);
    void  cardCheck (String cardNumberFrom, String cardNumberTo);
    void cvvCheck (String cvv);
    void dateCheck (String period);
    void balanceChange(int operationId);
    Response confirmOperation (OperationRequest operationRequest);
}
