package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.service.RoomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RoomController {

    @Resource
    private RoomService roomService;
}