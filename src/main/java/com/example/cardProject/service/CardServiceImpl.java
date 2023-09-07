package com.example.cardProject.service;

import com.example.cardProject.exception.NonConfirmException;
import com.example.cardProject.exception.TransferErrorException;
import com.example.cardProject.model.*;
import com.example.cardProject.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardVerification cardVerification;

    @Override
    public Response transfer(Request transferRequest) {
        String fromCardNumber = transferRequest.getNumberFrom();
        String toCardNumber = transferRequest.getNumberTo();
        String cvv = transferRequest.getCvv();
        String period = transferRequest.getValidTill();
        int operationId = cardRepository.getId();
        final Amount amount = transferRequest.getAmount();

        cardVerification.cvvCheck(cvv);
        cardVerification.cardCheck(fromCardNumber, toCardNumber);
        cardVerification.dateCheck(period);
        cardVerification.balanceChange(operationId);


        final Amount amountFromTestingCard = new Amount(1000000, "RUR");
        final Amount amountToTestingCard = new Amount(0, "RUR");
        final Card currentTestingCardFrom = new Card(fromCardNumber, period, cvv, amount);
        final Card currentTestingCardTo = new Card(toCardNumber, "10/27", "097", amountToTestingCard);
        int commission = amount.getValue() / 100;
        if (cardVerification.balanceFromCardVerification(currentTestingCardFrom, commission, amount)) {
            cardRepository.saveCard(currentTestingCardFrom, currentTestingCardTo);
            final String confirmationCode = String.valueOf((int) (Math.random() * (9999 - 1000) + 1000));
            cardRepository.saveTransfer(operationId, transferRequest);
            cardRepository.saveCode(operationId, confirmationCode);
            log.info("Новый перевод: Operation id {}, CardFrom {}, CardTo {}, amount {}, currency {}, commission {}",
                    operationId, fromCardNumber, toCardNumber, amount.getValue(), amount.getCurrency(), commission);
          return new Response(operationId);


        }
        throw new TransferErrorException("Transfer blocked");
    }

    @Override
    public Response confirmOperation(OperationRequest operationRequest) {
        int operationId = operationRequest.operationId();
        String operationCode = operationRequest.operationCode();
        if (operationCode.equals(cardRepository.getCode(operationId)) || operationCode.equals("0000")) {
            cardVerification.balanceChange(operationId);
            log.info("Операция одобрена");
            return new Response(operationId);

        } else log.info("Операция не была подтверждена, отказано в списании денежных средств");
        throw new NonConfirmException("Неверный код");
    }
}

