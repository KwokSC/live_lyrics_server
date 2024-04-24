package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.request.PaymentRequest;
import com.chunkie.live_lyrics_server.entity.response.PaymentResponse;
import com.squareup.square.SquareClient;
import com.squareup.square.api.PaymentsApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.*;
import com.squareup.square.models.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TipService {

    @Resource
    private SquareClient squareClient;

    private final static Logger logger = LoggerFactory.getLogger(TipService.class);

    public ResponseObject payment(PaymentRequest payload){
        Money bodyAmountMoney = new Money.Builder().amount(Long.valueOf(payload.getAmountMoney().getAmount()))
                .currency(payload.getAmountMoney().getCurrency())
                .build();

        CreatePaymentRequest request = new CreatePaymentRequest.Builder(
                payload.getSourceId(),
                payload.getIdempotencyKey(),
                bodyAmountMoney)
                .verificationToken(payload.getVerificationToken())
                .locationId(payload.getLocationId())
                .build();

        PaymentResponse response = new PaymentResponse();
        PaymentsApi paymentsApi = squareClient.getPaymentsApi();
        paymentsApi.createPaymentAsync(request)
                .thenApply(result -> {
                    Payment payment = result.getPayment();
                    response.setPaymentId(payment.getId());
                    response.setPaymentStatus(payment.getStatus());
                    response.setReceiptUrl(payment.getReceiptUrl());
                    response.setOrderId(payment.getOrderId());
                    return new ResponseObject(response, 200, "Payment created");
                }).exceptionally(exception -> null)
                .join();
        return ResponseObject.fail(null, "Payment failed");
    }

    public ResponseObject listLocations() {
        ResponseObject responseObject = new ResponseObject();
        squareClient.getLocationsApi().listLocationsAsync().thenAccept(result -> {
            responseObject.setCode(200);
            responseObject.setData(result.getLocations());
            responseObject.setMsg("Success to list locations");
        }).exceptionally(exception -> {
            try {
                throw exception.getCause();
            } catch (ApiException ae) {
                for (Error err : ae.getErrors()) {
                    System.out.println(err.getCategory());
                    System.out.println(err.getCode());
                    System.out.println(err.getDetail());
                }
            } catch (Throwable t) {
                logger.error("Unexpected error", t);
            }
            return null;
        }).join();
        return responseObject;
    }
}
