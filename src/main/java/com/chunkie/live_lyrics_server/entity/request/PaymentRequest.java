package com.chunkie.live_lyrics_server.entity.request;

import com.chunkie.live_lyrics_server.entity.Money;
import lombok.Data;

@Data
public class PaymentRequest {

    private String idempotencyKey;
    private String locationId;
    private String sourceId;
    private String verificationToken;
    private Money amountMoney;
}
