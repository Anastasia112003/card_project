package com.example.cardProject.controller;

import com.example.cardProject.exception.ErrorInputDataException;
import com.example.cardProject.exception.ExceptionWebResponse;
import com.example.cardProject.exception.NonConfirmException;
import com.example.cardProject.exception.TransferErrorException;
import com.example.cardProject.model.OperationRequest;
import com.example.cardProject.model.Request;
import com.example.cardProject.model.Response;
import com.example.cardProject.service.CardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @ExceptionHandler(ErrorInputDataException.class)
    public ResponseEntity<ExceptionWebResponse> handleErrorInputDataException(@NonNull final ErrorInputDataException exc) {
        String message = " Incorrect card data entry: ";
        log.error(message + exc.getMessage());
        return new ResponseEntity<>(new ExceptionWebResponse(exc.getMessage(),400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferErrorException.class)
    public ResponseEntity<ExceptionWebResponse> handleErrorTransferException(@NonNull final TransferErrorException exc) {
        String message = " Transfer error: ";
        log.error(message + exc.getMessage());
        return new ResponseEntity<>(new ExceptionWebResponse(exc.getMessage(),500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NonConfirmException.class)
    public ResponseEntity<ExceptionWebResponse> handleConfirmOperationException(@NonNull final NonConfirmException exc) {
        String message = " Error confirmation: ";
        log.error(message + exc.getMessage());
        return new ResponseEntity<>(new ExceptionWebResponse(exc.getMessage(),400), HttpStatus.BAD_REQUEST);
    }
}
