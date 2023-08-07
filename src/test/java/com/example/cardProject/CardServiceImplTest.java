package com.example.cardProject;

import com.example.cardProject.model.*;
import com.example.cardProject.repository.CardRepositoryImpl;
import com.example.cardProject.service.CardServiceImpl;
import com.example.cardProject.service.CardVerification;
import com.example.cardProject.service.CardVerificationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class CardServiceImplTest {
    @InjectMocks
    CardServiceImpl cardServiceImpl;
    @InjectMocks
    CardVerificationImpl cardVerificationImpl;

    @Mock
    CardRepositoryImpl cardRepositoryImpl;

    public static final String OPERATION_ID = "1";
    public static final String CONFIRMATION_CODE = "1234";
    public static final Request CARD_REQUEST = new Request("1111222233334444", "01/29", "345"
            , new Amount(15000, "RUR"),"5555666677778888");
    public static final Card TEST_CARD_FROM = new Card("1111222233334444", "01/25", "345", new Amount(1000000, "RUR"));
    public static final Card TEST_CARD_TO = new Card("5555666677778888", "10/27", "097", new Amount(0, "RUR"));
    public static final String CARD_NUMBER_FROM = "1111222233334444";
    public static final String CARD_NUMBER_TO = "5555666677778888";
    @Test
    void transfer(){
//given
Card currentTestingCardFrom=TEST_CARD_FROM;
Card currentTestingCardTo=TEST_CARD_TO;
 String operationId=OPERATION_ID;
 Request transferRequest=CARD_REQUEST;
 final String confirmationCode = String.valueOf((int) (Math.random() * (9999 - 1000) + 1000));
 Response expected = new Response(Integer.parseInt(operationId));

//when

        doNothing().when(cardRepositoryImpl).saveCard(currentTestingCardFrom, currentTestingCardTo);
        doNothing().when(cardRepositoryImpl).saveTransfer(Integer.parseInt(operationId), transferRequest);
        doNothing().when(cardRepositoryImpl).saveCode(Integer.parseInt(operationId),confirmationCode);
//then
       Response transferResponseActual = cardServiceImpl.transfer(transferRequest);
        assertEquals(expected.operationId(), transferResponseActual.operationId());
    }
    @Test
        void confirmOperation() {
            //given
            OperationRequest operationRequest = new OperationRequest(Integer.parseInt(OPERATION_ID), CONFIRMATION_CODE);
           int operationId = operationRequest.operationId();

            String operationCodeFromRepository = CONFIRMATION_CODE;
            Response responseExpected = new Response(operationId);
            CardVerificationImpl spy = Mockito.spy(cardVerificationImpl);
            CardServiceImpl spy1 = Mockito.spy(cardServiceImpl);

            //when
            when(cardRepositoryImpl.getCode(operationId)).thenReturn(operationCodeFromRepository);
            Mockito.doNothing().when(spy).balanceChange(operationId);

            //then
            Response responseActual = spy1.confirmOperation(operationRequest);
            assertEquals(responseExpected.operationId(), responseActual.operationId());


        }


    @ParameterizedTest
    @CsvSource({
            "1111222233330000,1111222233334444",
            "5555555555555555,6666666666666667",
            "5555666677778888,9999888877776666"
    })
    void cardCheck(String cardFromNumber, String cardToNumber) {

        assertNotNull(cardFromNumber);
        assertNotNull(cardToNumber);
        assertThat(cardFromNumber).matches("[0-9]{16}");
        assertThat(cardToNumber).matches("[0-9]{16}");
        assertThat(cardFromNumber).isNotEqualTo(cardToNumber);
    }

    @ParameterizedTest
    @CsvSource({
            "111",
            "555",
            "345",
            "000",
            "987"
    })
    void cvvCheck(String cardCVV) {
        assertNotNull(cardCVV);
        assertThat(cardCVV).containsOnlyDigits();
        assertThat(cardCVV).matches("\\d{3}");
    }
    @ParameterizedTest
    @CsvSource({
            "10/35",
            "12/25",
            "05/28",
            "11/29",
            "01/32"
    })
    void dateCheck(String cardFromValidTill) {

        assertThat(cardFromValidTill).isNotNull();

        final String[] yearAndMonth = cardFromValidTill.split("/");
        final int enteredMonth = Integer.parseInt(yearAndMonth[0]);
        final int enteredYear = Integer.parseInt(yearAndMonth[1]) + 2000;
        assertTrue(enteredMonth <= 12);
        assertTrue(enteredMonth >= 1);
        assertTrue(enteredYear > LocalDate.now().getYear());

    }

    @Test
    void balanceChange() {
        //given
        String operationId = OPERATION_ID;
        Request requestData = new Request("1111222233334444", "01/29",
                "345", new Amount(15000, "RUR"),"5555666677778888");
        String cardNumberFrom = CARD_NUMBER_FROM;
        String cardNumberTo = CARD_NUMBER_TO;

        Card cardFrom = TEST_CARD_FROM;
        Card cardTo = TEST_CARD_TO;
        int newBalanceCardFromExpected = 984850;
        int newBalanceCardToExpected = 15000;

        //when
        when(cardRepositoryImpl.getTransferRequest(Integer.parseInt(operationId))).thenReturn(requestData);
        when(cardRepositoryImpl.getCard(cardNumberFrom)).thenReturn(cardFrom);
        when(cardRepositoryImpl.getCard(cardNumberTo)).thenReturn(cardTo);

        //then
        cardVerificationImpl.balanceChange(Integer.parseInt(operationId));

        assertEquals(newBalanceCardFromExpected, cardFrom.getAmount().getValue());
        assertEquals(newBalanceCardToExpected, cardTo.getAmount().getValue());
    }
}
