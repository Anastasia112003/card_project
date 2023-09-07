package com.example.cardProject.repository;

import com.example.cardProject.model.Card;
import com.example.cardProject.model.Request;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CardRepositoryImpl implements CardRepository{

    private final Map<Integer, Request> transfers = new ConcurrentHashMap();
    private final Map<String, Card> cards = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, String> codes = new ConcurrentHashMap();
    private final AtomicInteger operationId = new AtomicInteger();

    @Override
    public void saveTransfer(int operationId, Request request) {
        transfers.put(operationId, request);
    }

    @Override
    public Request getTransferRequest(int operationId) {
        return transfers.get(operationId);
    }


    @Override
    public int getId() {
        return operationId.incrementAndGet();
    }


    @Override
    public void saveCode(int operationId, String confirmationCode) {
        codes.put(operationId, confirmationCode);
    }

    @Override
    public String getCode(int operationId) {
        return   codes.get(operationId);
    }

    @Override
    public void saveCard(Card currentTestingCardFrom, Card currentTestingCardTo) {
        cards.put(currentTestingCardFrom.getNumber(),currentTestingCardFrom);
        cards.put(currentTestingCardTo.getNumber(), currentTestingCardTo);
    }

    @Override
    public Card getCard(String cardNumber) {
        return cards.get(cardNumber);
    }
}
