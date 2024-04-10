package com.chunkie.live_lyrics_server.service;

import com.squareup.square.SquareClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DonationService {

    @Resource
    private SquareClient squareClient;

}
