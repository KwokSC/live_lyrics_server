package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.Constants;
import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.User;
import com.chunkie.live_lyrics_server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/login")
    public ResponseObject login(@RequestBody User user){
        String token = userService.authenticateUser(user);
        return token!=null ? ResponseObject.success(token, "Successfully login.") : ResponseObject.fail(null, "Fail to login.");
    }
}
