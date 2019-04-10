package com.cust.service;

import com.cust.Entity.User;
import com.cust.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public String selectUserOpenId(String openid) {
        User userInfo = userMapper.selectUser(openid);
        if (userInfo != null) {
            return userInfo.getId();
        } else {
            return null;
        }
    }

    public boolean insertUserInfo(User userInfo) {

        return userMapper.insertUserInfo(userInfo) > 0 ? true : false;
    }
}
