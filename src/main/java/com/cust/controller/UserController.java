package com.cust.controller;

import com.cust.Entity.User;
import com.cust.Utils.Token;
import com.cust.Utils.WxUtils;
import com.cust.service.UserService;
import easy.web.RequestTool;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private WxUtils wxUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 获取前端code，此方法用于发送请求到auth.code2Session接口获取用户openid以及sessionkey
     * map应包含昵称，国家，城市等微信基本用户信息
     * <p>
     * errcode:
     * -1	系统繁忙，此时请开发者稍候再试
     * 0	请求成功
     * 40029	code 无效
     * 45011	频率限制，每个用户每分钟100次
     *
     * @param request
     * @return repMap
     * status：
     * -1   登陆失败
     * 0    登陆成功
     */
    @RequestMapping("/WeChatlogin")
    public Map wxUserLogin(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Map map = RequestTool.getParameterMap(request);
        System.out.println(map);
        Map<String, String> repMap = new HashMap();
        if (!map.containsKey("code")) {
            repMap.put("status", "-1");
            return repMap;
        }
        String userOpenId = wxUtils.oauth2GetOpenid((String) map.get("code"));
        System.out.println(userOpenId);
        try {
            //String转为json
            JSONObject usrOpenIdAndSessionKey = (JSONObject) (new JSONParser().parse(userOpenId));
            if (!usrOpenIdAndSessionKey.containsKey("errcode")) {
                //获取openid成功
                String openid = (String) usrOpenIdAndSessionKey.get("openid");
                System.out.println(openid);
                //产生第三方会话密钥
                String thirdSessionKey = Token.CreateToken();
                //查询此openid是否存在
                String id = userService.selectUserOpenId(openid);
                if (id != null && !id.equals("")) {
                    //此用户为老用户
                    //返回用户数据库中唯一id
                    repMap.put("id", id);
                    repMap.put("thirdSessionKey", thirdSessionKey);
                } else {
                    //此用户为新用户
                    JSONObject wxuser = (JSONObject) (new JSONParser().parse((String) map.get("rawData")));
                    User userInfo = new User();
                    String newId = Token.createNewUserId();
                    userInfo.setAvatarURL((String) wxuser.get("avatarUrl"));
                    userInfo.setId(newId);
                    userInfo.setCity(String.valueOf(wxuser.get("city")));
                    userInfo.setCountry(String.valueOf(wxuser.get("country")));
                    userInfo.setGender(Integer.parseInt(String.valueOf(wxuser.get("gender"))));
                    userInfo.setNickname(String.valueOf(wxuser.get("nickName")));
                    userInfo.setOpenid(openid);
                    userInfo.setProvince(String.valueOf(wxuser.get("province")));
                    if (userService.insertUserInfo(userInfo)) {
                        //插入新用户成功
                        repMap.put("id", newId);
                        repMap.put("thirdSessionKey", thirdSessionKey);
                    } else {
                        //插入新用户失败
                        repMap.put("status", "-1");
                        return repMap;
                    }
                }
                //更新redis
                String key = (String) redisTemplate.opsForValue().get("openid");
                if (key != null) {
                    redisTemplate.delete(redisTemplate.opsForValue().get(key));
                    redisTemplate.delete(key);
                }
                redisTemplate.opsForValue().set(openid, repMap.get("thirdSessionKey"));
                redisTemplate.opsForValue().set(repMap.get("thirdSessionKey"), repMap.get("id"));
                //登陆成功
                repMap.remove("id");
                repMap.put("status", "0");
                return repMap;
            } else {
                //获取用户openid失败
                repMap.put("status", "-1");
                return repMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        repMap.put("status", "-1");
        return repMap;
    }

    /**
     * 获取服务器产生的第三方会话密钥，查询redis进行快速登陆（小程序没有cookie机制）
     *
     * @param request
     * @return
     */
    @RequestMapping("/uuidLogin")
    public Map uuidLogin(HttpServletRequest request) {
        Map map = RequestTool.getParameterMap(request);
        Map<String, String> reqMap = new HashMap<>();
        String thirdSessionKey = String.valueOf(map.get("thirdSessionKey"));
        //查询redis进行登陆
        String id = (String) redisTemplate.opsForValue().get(thirdSessionKey);
        if (id != null) {
            reqMap.put("status", "0");
            return reqMap;
        }
        return null;
    }
}
