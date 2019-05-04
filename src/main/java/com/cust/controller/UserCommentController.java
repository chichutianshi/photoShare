package com.cust.controller;

import com.cust.Entity.Photocomment;
import com.cust.Utils.Token;
import com.cust.service.UserCommentService;
import com.cust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class UserCommentController {


    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    UserCommentService userCommentService;

    @Autowired
    UserService userService;

    /**
     * 获取redis中缓存的主评论
     *
     * @param request 前端携带photoId
     * @return
     */
    @RequestMapping("/getMainPhotoComment")
    public List getMainPhotoComment(HttpServletRequest request) {
        String photoId = String.valueOf(request.getParameter("photoId"));
        String commentIndex = String.valueOf(request.getParameter("commentIndex"));
        if (Integer.valueOf(commentIndex) >= redisTemplate.opsForList().size(photoId) && Integer.valueOf(commentIndex) != 0) {
            return new ArrayList();
        }
        List mainComments = redisTemplate.opsForList().range(photoId, Integer.valueOf(commentIndex), 10);
        if (mainComments == null || mainComments.size() == 0) {
            mainComments = userCommentService.getMainComment(photoId);
        }
        return mainComments;
    }

    /**
     * 评论页面下拉刷新（直接从数据库中获取）
     *
     * @param request
     * @return
     */
    @RequestMapping("/refreshComments")
    public List refreshComments(HttpServletRequest request) {
        String photoId = String.valueOf(request.getParameter("photoId"));
        List mainComments = userCommentService.getMainComment(photoId);
        return mainComments;
    }


    /**
     * 发布主评论
     * 返回值：
     * 1：发布成功
     * -1：发布失败
     * -2：未登录，前端跳转到登陆界面
     *
     * @param request
     * @return
     */
    @RequestMapping("/publishMainComment")
    public int publishMainComment(HttpServletRequest request) {
        String thirdSessionKey = request.getParameter("thirdSessionKey");
        //fromId:获取发布主评论人的userId
        String fromId = (String) redisTemplate.opsForValue().get(thirdSessionKey);
//        System.out.println(thirdSessionKey + ":" + fromId);
        String content = request.getParameter("content");
//        System.out.println(content);
        String photoId = request.getParameter("photoId");
//        System.out.println(photoId);

        if (fromId == null) {
            return -2;
        }

        Map userInfo = (Map) redisTemplate.opsForValue().get(fromId);

        if (userInfo == null) {
            userInfo = userService.getPublisherInfo(fromId);
            if (userInfo == null) {
                return -2;
            }
        }
        System.out.println(userInfo);
        Photocomment photocomment = new Photocomment();
        photocomment.setContent(content);
        photocomment.setFromid(fromId);
        photocomment.setPhotoid(photoId);
        photocomment.setFromname((String) userInfo.get("nickname"));
        photocomment.setFromurl((String) userInfo.get("avatarURL"));
        photocomment.setId(Token.createNewUserId());
        photocomment.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (!userCommentService.publishMainComment(photocomment)) {
            return -1;
        }
        //同步redis中的评论
        redisTemplate.opsForList().rightPush(photoId, photocomment);
        return 1;
    }
}
