package com.example.cardProject.service;

import com.example.cardProject.exception.ErrorInputDataException;
import com.example.cardProject.model.Amount;
import com.example.cardProject.model.Card;
import com.example.cardProject.model.Request;
import com.example.cardProject.repository.CardRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
@Slf4j
@RequiredArgsConstructor
public class CardVerificationImpl implements CardVerification{
    private final CardRepositoryImpl cardRepository;

    public boolean balanceFromCardVerification(Card cardFrom, int commission, Amount amountOperation) {
        int balanceOnCard = cardFrom.getAmount().getValue();
        int amountWithCommission = amountOperation.getValue() + commission;
        if (balanceOnCard < amountWithCommission) {
            return false;
        }
        return true;
    }

    @Override
    public void cardCheck(String cardNumberFrom, String cardNumberTo) {
        if (cardNumberFrom == null || cardNumberTo == null) {
            throw new ErrorInputDataException("Вы не введи данные карты");
        } else if (!cardNumberFrom.matches("[0-9]{16}") || !cardNumberTo.matches("[0-9]{16}")) {
            throw new ErrorInputDataException("Номер карты должен состоять из 16 чисел");
        } else if (cardNumberFrom.equals(cardNumberTo)) {
            throw new ErrorInputDataException("Номера карт не могут быть одинаковы");

        }

    }

    @Override
    public void cvvCheck(String cvv) {
        if (cvv == null || !cvv.matches("\\d{3}") || !cvv.chars().allMatch(Character::isDigit)) {
            throw new ErrorInputDataException("Данные  CVV не верны ");
        }
    }

    @Override
    public void dateCheck(String period) {
        if (period == null) {
            throw new ErrorInputDataException("Введите срок действия карты");
        }
        String[] data = period.split("/");
        final int month = Integer.parseInt(data[0]);
        final int year = Integer.parseInt(data[1]) + 2000;
        if (month > 12 || month < 1) {
            throw new ErrorInputDataException("Номер месяца может быть от 1 до 12");
        }

        if (year < LocalDate.now().getYear()) {
            throw new ErrorInputDataException("Истек срок действия вашей карты ");
        }
        if (year == LocalDate.now().getYear() && month <= LocalDate.now().getMonthValue()) {
            throw new ErrorInputDataException("Истек срок действия вашей карты");
        }


    }



@Override
    public void balanceChange(int operationId) {
        Request requestInfo = cardRepository.getTransferRequest(operationId);
        Amount amount = requestInfo.getAmount();
        String cardNumberFrom = requestInfo.getNumberFrom();
        String cardNumberTo = requestInfo.getNumberTo();
        int commission = amount.getValue() / 100;
        Card cardFrom = cardRepository.getCard(cardNumberFrom);
        Card cardTo = cardRepository.getCard(cardNumberTo);
        int newBalanceCardFrom = cardFrom.getAmount().getValue() - (amount.getValue() + commission);
        cardFrom.getAmount().setValue(newBalanceCardFrom);
        int newBalanceCardTo = cardTo.getAmount().getValue() + amount.getValue();
        cardTo.getAmount().setValue(newBalanceCardTo);
        log.info("Баланс карты отправителя {} равен {} ", cardNumberFrom, cardRepository.getCard(cardNumberFrom).getAmount());
        log.info("Баланс карты получателя {} равен {} ", cardNumberTo, cardRepository.getCard(cardNumberTo).getAmount());
    }
}
