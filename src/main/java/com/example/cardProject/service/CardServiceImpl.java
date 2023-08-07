package com.example.cardProject.service;

import com.example.cardProject.exception.ErrorInputDataException;
import com.example.cardProject.exception.NonConfirmException;
import com.example.cardProject.exception.TransferErrorException;
import com.example.cardProject.model.*;
import com.example.cardProject.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        final Card currentTestingCardFrom = new Card(fromCardNumber, period, cvv, amountFromTestingCard);
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

//    public boolean balanceFromCardVerification(Card cardFrom, int commission, Amount amountOperation) {
//        int balanceOnCard = cardFrom.getAmount().getValue();
//        int amountWithCommission = amountOperation.getValue() + commission;
//        if (balanceOnCard < amountWithCommission) {
//            return false;
//        }
//        return true;
//    }
//
//
//    @Override
//    public void cardCheck(String cardNumberFrom, String cardNumberTo) {
//        if (cardNumberFrom == null || cardNumberTo == null) {
//            throw new ErrorInputDataException("Вы не введи данные карты");
//        } else if (!cardNumberFrom.matches("[0-9]{16}") || !cardNumberTo.matches("[0-9]{16}")) {
//            throw new ErrorInputDataException("Номер карты должен состоять из 16 чисел");
//        } else if (cardNumberFrom.equals(cardNumberTo)) {
//            throw new ErrorInputDataException("Номера карт не могут быть одинаковы");
//
//        }
//
//    }
//
//    @Override
//    public void cvvCheck(String cvv) {
//        if (cvv == null || !cvv.matches("\\d{3}") || !cvv.chars().allMatch(Character::isDigit)) {
//            throw new ErrorInputDataException("Данные  CVV не верны ");
//        }
//    }
//
//    @Override
//    public void dateCheck(String period) {
//        if (period == null) {
//            throw new ErrorInputDataException("Введите срок действия карты");
//        }
//        String[] data = period.split("/");
//        final int month = Integer.parseInt(data[0]);
//        final int year = Integer.parseInt(data[1]) + 2000;
//        if (month > 12 || month < 1) {
//            throw new ErrorInputDataException("Номер месяца может быть от 1 до 12");
//        }
//
//        if (year < LocalDate.now().getYear()) {
//            throw new ErrorInputDataException("Истек срок действия вашей карты ");
//        }
//        if (year == LocalDate.now().getYear() && month <= LocalDate.now().getMonthValue()) {
//            throw new ErrorInputDataException("Истек срок действия вашей карты");
//        }
//
//
//    }
//
//    @Override
//    public void balanceChange(int operationId) {
//        Request requestInfo = cardRepository.getTransferRequest(operationId);
//        Amount amount = requestInfo.getAmount();
//        String cardNumberFrom = requestInfo.getNumberFrom();
//        String cardNumberTo = requestInfo.getNumberTo();
//        int commission = amount.getValue() / 100;
//        Card cardFrom = cardRepository.getCard(cardNumberFrom);
//        Card cardTo = cardRepository.getCard(cardNumberTo);
//        int newBalanceCardFrom = cardFrom.getAmount().getValue() - (amount.getValue() + commission);
//        cardFrom.getAmount().setValue(newBalanceCardFrom);
//        int newBalanceCardTo = cardTo.getAmount().getValue() + amount.getValue();
//        cardTo.getAmount().setValue(newBalanceCardTo);
//        log.info("Баланс карты отправителя {} равен {} ", cardNumberFrom, cardRepository.getCard(cardNumberFrom).getAmount());
//        log.info("Баланс карты получателя {} равен {} ", cardNumberTo, cardRepository.getCard(cardNumberTo).getAmount());
//    }

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

