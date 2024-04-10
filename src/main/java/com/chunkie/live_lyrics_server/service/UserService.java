package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.entity.Profile;
import com.chunkie.live_lyrics_server.entity.User;
import com.chunkie.live_lyrics_server.entity.UserInfo;
import com.chunkie.live_lyrics_server.entity.response.LoginResponse;
import com.chunkie.live_lyrics_server.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthService authService;

    @Resource
    private S3Service s3Service;

    private final String ALBUM_IMAGE_PREFIX = "resources/images/profile/";

    public void register(User user) {
        user.setUserId(generateUserId());

    }

    public User getUserById(String id){
        return userMapper.getUserById(id);
    }

    public User getUserByAccount(String account){
        return userMapper.getUserByAccount(account);
    }

    public Profile getProfileByAccount(String account){
        User user = userMapper.getUserByAccount(account);
        Profile profile = new Profile();
        if(user != null){
            profile.setUserAccount(user.getUserAccount());
            profile.setUserName(user.getUserName());
            profile.setGender(String.valueOf(user.getGender()));
            profile.setSummary(user.getSummary());
            profile.setProfileImg(s3Service.getFile(ALBUM_IMAGE_PREFIX + user.getUserId()));
        }
        return profile;
    }

    public LoginResponse authenticateUser(User user){
        User u = userMapper.getUserByAccount(user.getUserAccount());
        if (u != null && u.getUserPassword().equals(user.getUserPassword())){
            LoginResponse response = new LoginResponse();
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(u.getUserId());
            userInfo.setUserAccount(u.getUserAccount());
            userInfo.setUserName(u.getUserName());
            response.setAuth(authService.generateToken(u.getUserAccount()));
            response.setUserInfo(userInfo);
            return response;
        }else
            return null;
    }

    private String generateUserId() {
        String uuid = UUID.randomUUID().toString();
        return "user_"+uuid.substring(uuid.length() - 6);
    }
}
