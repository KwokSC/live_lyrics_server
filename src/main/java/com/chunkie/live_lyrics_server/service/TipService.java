package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.request.PaymentRequest;
import com.chunkie.live_lyrics_server.entity.response.PaymentResponse;
import com.squareup.square.SquareClient;
import com.squareup.square.api.PaymentsApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TipService {

    @Resource
    private SquareClient squareClient;

    private final static Logger logger = LoggerFactory.getLogger(TipService.class);

    public ResponseObject payment(PaymentRequest payload) {
        Money money = new Money.Builder().amount(Long.valueOf(payload.getAmountMoney().getAmount()))
                .currency(payload.getAmountMoney().getCurrency())
                .build();

        CreatePaymentRequest request = new CreatePaymentRequest.Builder(
                payload.getSourceId(),
                payload.getIdempotencyKey())
                .amountMoney(money)
                .verificationToken(payload.getVerificationToken())
                .locationId(payload.getLocationId())
                .build();

        PaymentsApi paymentsApi = squareClient.getPaymentsApi();
        return paymentsApi.createPaymentAsync(request)
                .thenApply(result -> ResponseObject.success(result.getPayment(), "Payment created")).exceptionally(exception -> {
                    ApiException e = (ApiException) exception.getCause();
                    logger.info("Exception: {}", e.getMessage());
                    return ResponseObject.fail(null, "Payment failed");
                }).join();
    }
}
