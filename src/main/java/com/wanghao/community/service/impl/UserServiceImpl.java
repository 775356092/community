package com.wanghao.community.service.impl;

import com.wanghao.community.dao.UserMapper;
import com.wanghao.community.entity.User;
import com.wanghao.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(int id) {
        return userMapper.selectById(id);
    }
}
