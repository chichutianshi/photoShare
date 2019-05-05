package com.cust.controller;

import com.cust.Entity.Commentreply;
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

@RestController
public class UserCommentController {


    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    UserCommentService userCommentService;


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
     * 前端携带主评论photoId，thirdSessionKey,content,nickName,avatarUrl
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

        String avatarUrl = request.getParameter("avatarUrl");
        String nickName = request.getParameter("nickName");

        if (fromId == null) {
            return -2;
        }
        Photocomment photocomment = new Photocomment();
        photocomment.setContent(content);
        photocomment.setFromid(fromId);
        photocomment.setPhotoid(photoId);
        photocomment.setFromname(nickName);
        photocomment.setFromurl(avatarUrl);
        photocomment.setId(Token.createNewUserId());
        photocomment.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (!userCommentService.publishMainComment(photocomment)) {
            return -1;
        }
        //同步redis中的评论
        redisTemplate.opsForList().rightPush(photoId, photocomment);
        return 1;
    }


    /**e
     * 获取子评论
     * 前端携带主评论id
     *
     * @param request
     * @return
     */
    @RequestMapping("/getSonComments")
    public List getSonComments(HttpServletRequest request) {
        String commentId = request.getParameter("commentId");
        String commentIndex = String.valueOf(request.getParameter("commentIndex"));
        if (Integer.valueOf(commentIndex) >= redisTemplate.opsForList().size(commentId) && Integer.valueOf(commentIndex) != 0) {
            return new ArrayList();
        }
        List sonComments = redisTemplate.opsForList().range(commentId, Integer.valueOf(commentIndex), 10);
        if (sonComments == null || sonComments.size() == 0) {
            sonComments = userCommentService.getSonComment(commentId);
        }
        return sonComments;
    }

    /**
     * 刷新子评论页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/refreshSonComments")
    public List refreshSonComments(HttpServletRequest request) {
        String commentId = request.getParameter("commentId");
        List sonComments = userCommentService.getSonComment(commentId);
        return sonComments;
    }

    /**
     * 子评论发布
     * 前端携带主评论commentId，thirdSessionKey,content,nickName,avatarUrl
     * 返回值：
     * 1：发布成功
     * -1：发布失败
     * -2：未登录，前端跳转到登陆界面
     *
     * @param request
     * @return
     */
    @RequestMapping("/commentsReply")
    public int commentsReply(HttpServletRequest request) {
        String thirdSessionKey = request.getParameter("thirdSessionKey");
        String fromId = (String) redisTemplate.opsForValue().get(thirdSessionKey);
        String content = request.getParameter("content");
        String commentId = request.getParameter("commentId");
        String avatarUrl = request.getParameter("avatarUrl");
        String nickName = request.getParameter("nickName");

        if (fromId == null) {
            return -2;
        }
        Commentreply commentreply = new Commentreply();
        commentreply.setCommentid(commentId);
        commentreply.setContent(content);
        commentreply.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        commentreply.setFromid(fromId);
        commentreply.setFromname(nickName);
        commentreply.setFromurl(avatarUrl);

        if (!userCommentService.publishSonComment(commentreply)) {
            return -1;
        }
        redisTemplate.opsForList().rightPush(commentId, commentreply);
        return 1;
    }
}
