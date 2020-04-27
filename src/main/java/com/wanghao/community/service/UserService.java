package com.wanghao.community.service;

import com.wanghao.community.entity.LoginTicket;
import com.wanghao.community.entity.User;

import java.util.Map;

public interface UserService {
    User findUserById(int id);
    Map<String, Object> register(User user);
    int activation(int userId,String key);
    Map<String, Object> login(String username,String password,int expiredSeconds);
    void logout(String ticket);
    LoginTicket findByTicket(String ticket);
}
