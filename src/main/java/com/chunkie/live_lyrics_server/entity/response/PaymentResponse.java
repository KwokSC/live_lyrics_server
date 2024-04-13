package com.chunkie.live_lyrics_server.entity.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentId;
    private String paymentStatus;
    private String receiptUrl;
    private String orderId;
}
