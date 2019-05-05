package com.cust.service;

import com.cust.Entity.Photocomment;
import com.cust.dao.PhotocommentMapper;
import com.cust.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserCommentService {

    @Autowired
    PhotocommentMapper photocommentMapper;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    public List<Map> getMainComment(String photoId) {
        List<Map> result = photocommentMapper.selectByPhotoId(photoId);
        if (result != null && result.size() != 0) {
            if (redisTemplate.opsForList().size(photoId) < result.size()) {
                if (redisTemplate.hasKey(photoId)) {
                    redisTemplate.delete(photoId);
                }
                for (Map comment : result) {
                    redisTemplate.opsForList().rightPush(photoId, comment);
                }
                redisTemplate.expire(photoId, 1, TimeUnit.DAYS);
            }

        }
        return result;
    }

    public boolean publishMainComment(Photocomment photocomment) {

        return photocommentMapper.insertMainComment(photocomment) > 0;
    }

}