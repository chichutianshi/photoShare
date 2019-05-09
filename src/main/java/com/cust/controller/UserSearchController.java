package com.cust.controller;


import com.cust.service.UserSearchService;
import easy.web.RequestTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserSearchController {
    @Autowired
    private UserSearchService userSearchService;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @RequestMapping("/loadPhotos")
    public List<Map> showPhotos(HttpServletRequest httpServletRequest) {
        Map reqMap = RequestTool.getParameterMap(httpServletRequest);
        String thirdSessionKey = (String) reqMap.get("thirdSessionKey");
        //如果reqMap中含有thirdSessionKey进行个性化推荐显示
        System.out.println(thirdSessionKey);
        if (thirdSessionKey.equals("")) {
            return userSearchService.selectAllPhotos(Integer.valueOf((String) (reqMap.get("selectRow"))));
        } else {
            System.out.println("asd");
        }
        return null;
    }

    /**
     * 点赞或者取消
     * 参数：photoId
     *
     * @param request
     * @return
     */
    @RequestMapping("/increaseOrDecreaseLike")
    public int increaseOrDecreaseLike(HttpServletRequest request) {
        String photoId = request.getParameter("photoId");
        String likeOrDislike = request.getParameter("likeOrDislike");
        String redisKey = "photoLikeNum" + photoId;
        if (likeOrDislike.equals("1")) {
            if (redisTemplate.hasKey(redisKey)) {
                redisTemplate.opsForValue().set(redisKey, (int) redisTemplate.opsForValue().get(redisKey) + 1);
            }
        }
        if (likeOrDislike.equals("0")) {
            if (redisTemplate.hasKey(redisKey)) {
                redisTemplate.opsForValue().set(redisKey, (int) redisTemplate.opsForValue().get(redisKey) - 1);
            }
        }


        return 0;
    }
}