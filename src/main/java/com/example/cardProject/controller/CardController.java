package com.example.cardProject.controller;


import com.example.cardProject.model.OperationRequest;
import com.example.cardProject.model.Request;
import com.example.cardProject.model.Response;
import com.example.cardProject.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CardController {
    private final CardService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<Response> transfer(@RequestBody Request transferRequest) {
        Response response = transferService.transfer(transferRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<Response> confirmOperation(@RequestBody OperationRequest operationRequest) {
        Response response = transferService.confirmOperation(operationRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
