package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.entity.User;
import com.chunkie.live_lyrics_server.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthService authService;

    public String authenticateUser(User user){
        User u = userMapper.getUserById(user.getUserAccount());
        if (u != null && u.getUserPassword().equals(user.getUserPassword())){
            return authService.generateToken(u.getUserAccount());
        }else
            return null;
    }
}
