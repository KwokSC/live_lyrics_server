package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.request.PaymentRequest;
import com.chunkie.live_lyrics_server.service.TipService;
import com.google.gson.JsonObject;
import com.squareup.square.models.Card;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.Payment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/tip")
@RestController
public class TipController {

    @Resource
    private TipService tipService;

    @RequestMapping("/payment")
    public ResponseObject payment(@RequestBody PaymentRequest request){
        return tipService.payment(request);
    }

    @RequestMapping("/listLocations")
    public ResponseObject listLocations(){
        return tipService.listLocations();
    }
}
