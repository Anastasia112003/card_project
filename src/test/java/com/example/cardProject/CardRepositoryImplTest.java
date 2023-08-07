package com.example.cardProject;

import com.example.cardProject.model.Amount;
import com.example.cardProject.model.Card;
import com.example.cardProject.model.Request;
import com.example.cardProject.repository.CardRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;


@SpringBootTest
public class CardRepositoryImplTest {
    @Autowired
    CardRepositoryImpl cardRepositoryImpl ;

    public static final String OPERATION_ID = "1";
    public static final int ID = 1;
    public static final String CONFIRMATION_CODE = "4580";
    public static final Request TRANSFER_REQUEST = new Request("1111222233334444", "01/29", "345",
            new Amount(15000, "RUR"),"5555666677778888");
    public static final Card TEST_CARD_FROM = new Card("5555222200005555", "01/25", "345", new Amount(1000000, "RUR"));
    public static final Card TEST_CARD_TO = new Card("0000111100002222", "10/27", "097", new Amount(0, "RUR"));
    public static final String CARD_NUMBER_FROM = "5555222200005555";
    public static final String CARD_NUMBER_TO = "0000111100002222";

    @Test
    void saveTransfer() {
        String operationId = OPERATION_ID;
        Request requestExp = TRANSFER_REQUEST;
        cardRepositoryImpl.saveTransfer(Integer.parseInt(operationId), requestExp);

        Request requestActual = cardRepositoryImpl.getTransferRequest(Integer.parseInt(operationId));
        assertEquals(requestExp, requestActual);

    }


    @Test

    void getId() {
        int operationIdExpected = ID;
        int operationIdActual = cardRepositoryImpl.getId();
        assertEquals(operationIdExpected, operationIdActual);


    }

    @Test
    void saveCode() {

        String operationId = OPERATION_ID;
        String codeExpected = CONFIRMATION_CODE;
       cardRepositoryImpl.saveCode(Integer.parseInt(operationId), codeExpected);

        String codeActual = cardRepositoryImpl.getCode(Integer.parseInt(operationId));
        assertEquals(codeExpected, codeActual);

    }

    @Test
    void getCode() {
        String operationId = OPERATION_ID;
        String codeExpected = CONFIRMATION_CODE;
        cardRepositoryImpl.saveCode(Integer.parseInt(operationId), codeExpected);

        String codeActual = cardRepositoryImpl.getCode(Integer.parseInt(operationId));
        assertEquals(codeExpected, codeActual);

    }

    @Test
    void saveCard() {
        Card testCardFrom = TEST_CARD_FROM;
        Card testCardTo = TEST_CARD_TO;

        cardRepositoryImpl.saveCard(testCardFrom, testCardTo);

        Card cardFromActual = cardRepositoryImpl.getCard(CARD_NUMBER_FROM);
        Card cardToActual = cardRepositoryImpl.getCard(CARD_NUMBER_TO);

        assertEquals(testCardFrom, cardFromActual);
        assertEquals(testCardTo, cardToActual);

    }

    @Test
    void getCard() {

        Card testCardFrom = TEST_CARD_FROM;
        Card testCardTo = TEST_CARD_TO;

        cardRepositoryImpl.saveCard(testCardFrom, testCardTo);

        Card cardFromActual = cardRepositoryImpl.getCard(CARD_NUMBER_FROM);
        Card cardToActual = cardRepositoryImpl.getCard(CARD_NUMBER_TO);

        assertEquals(testCardFrom, cardFromActual);
        assertEquals(testCardTo, cardToActual);

    }
}
