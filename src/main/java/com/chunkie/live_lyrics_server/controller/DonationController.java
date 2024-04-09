package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.service.DonationService;
import com.squareup.square.models.Card;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/donation")
@RestController
public class DonationController {

    @Resource
    private DonationService donationService;

    @RequestMapping("/pay")
    public ResponseObject pay(@RequestBody Card card){
        return new ResponseObject();
    }
}
