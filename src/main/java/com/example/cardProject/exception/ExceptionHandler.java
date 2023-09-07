package com.example.cardProject.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ErrorInputDataException.class)
    public ResponseEntity<ExceptionWebResponse> handleErrorInputDataException(@NonNull final ErrorInputDataException exc) {
        String message = " Incorrect card data entry: ";
        log.error(message + exc.getMessage());
        return new ResponseEntity<>(new ExceptionWebResponse(exc.getMessage(),400), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(TransferErrorException.class)
    public ResponseEntity<ExceptionWebResponse> handleErrorTransferException(@NonNull final TransferErrorException exc) {
        String message = " Transfer error: ";
        log.error(message + exc.getMessage());
        return new ResponseEntity<>(new ExceptionWebResponse(exc.getMessage(),500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NonConfirmException.class)
    public ResponseEntity<ExceptionWebResponse> handleConfirmOperationException(@NonNull final NonConfirmException exc) {
        String message = " Error confirmation: ";
        log.error(message + exc.getMessage());
        return new ResponseEntity<>(new ExceptionWebResponse(exc.getMessage(),400), HttpStatus.BAD_REQUEST);
    }
}
