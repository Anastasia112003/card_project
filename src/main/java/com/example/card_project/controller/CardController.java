package com.example.card_project.controller;

import com.example.card_project.model.OperationRequest;
import com.example.card_project.model.Request;
import com.example.card_project.model.Response;
import com.example.card_project.service.CardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {
    private final CardService transferService;

    public CardController(CardService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public Response transfer (@RequestBody Request transferRequest) {
        Response response = transferService.transfer(transferRequest);
        return response;
    }

    @PostMapping("/confirmOperation")
    public Response confirmOperation (@RequestBody OperationRequest operationRequest) {
        Response response = transferService.confirmOperation(operationRequest);
        return response;
    }
}
