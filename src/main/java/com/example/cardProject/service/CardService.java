package com.example.cardProject.service;

import com.example.cardProject.model.OperationRequest;
import com.example.cardProject.model.Request;
import com.example.cardProject.model.Response;

public interface CardService {
    Response transfer(Request transferRequest);
    Response confirmOperation (OperationRequest operationRequest);
}
