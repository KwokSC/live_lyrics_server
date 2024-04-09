package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.dto.UserDTO;
import com.chunkie.live_lyrics_server.entity.Profile;
import com.chunkie.live_lyrics_server.entity.User;
import com.chunkie.live_lyrics_server.entity.UserInfo;
import com.chunkie.live_lyrics_server.entity.response.LoginResponse;
import com.chunkie.live_lyrics_server.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthService authService;

    public User getUserById(String id){
        return userMapper.getUserById(id);
    }

    public Profile getProfileById(String id){
        User user = userMapper.getUserById(id);
        Profile profile = new Profile();
        if(user != null){
            profile.setUserAccount(user.getUserAccount());
            profile.setUserName(user.getUserName());
            profile.setGender(String.valueOf(user.getGender()));
            profile.setSummary(user.getSummary());
        }
        return profile;
    }

    public LoginResponse authenticateUser(User user){
        User u = userMapper.getUserById(user.getUserAccount());
        if (u != null && u.getUserPassword().equals(user.getUserPassword())){
            LoginResponse response = new LoginResponse();
            UserInfo userInfo = new UserInfo();
            userInfo.setUserAccount(u.getUserAccount());
            userInfo.setUserName(u.getUserName());
            response.setAuth(authService.generateToken(u.getUserAccount()));
            response.setUserInfo(userInfo);
            return response;
        }else
            return null;
    }
}
