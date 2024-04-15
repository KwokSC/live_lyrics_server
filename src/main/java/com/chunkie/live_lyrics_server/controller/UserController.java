package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.User;
import com.chunkie.live_lyrics_server.entity.response.LoginResponse;
import com.chunkie.live_lyrics_server.service.AuthService;
import com.chunkie.live_lyrics_server.service.UserService;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private AuthService authService;

    @RequestMapping("/login")
    public ResponseObject login(@RequestBody User user) {
        LoginResponse response = userService.authenticateUser(user);
        return response != null ? ResponseObject.success(response, "Successfully login.") : ResponseObject.fail(null, "Fail to login.");
    }

    @RequestMapping("/getUserInfo")
    public ResponseObject getUserInfo(@RequestParam String token) {
        String userAccount = authService.getUserByToken(token);
        User user = userService.getUserByAccount(userAccount);
        if (user == null) {
            return new ResponseObject(null, HttpStatus.SC_UNAUTHORIZED, "Login expired.");
        } else {
            return new ResponseObject(userService.getUserInfo(user), HttpStatus.SC_OK, "Successfully logged in.");
        }
    }

    @RequestMapping("/getProfileByAccount")
    public ResponseObject getProfileByAccount(@RequestParam String account) {
        return ResponseObject.success(userService.getProfileByAccount(account), "Successfully obtain the profile.");
    }
}
