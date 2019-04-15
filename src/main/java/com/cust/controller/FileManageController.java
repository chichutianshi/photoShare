package com.cust.controller;

import com.cust.Utils.BaiduUtils;
import com.cust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/fileCtrl")
public class FileManageController {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private UserService userService;

    @RequestMapping("/upPicture")
    public Map upPicture(HttpServletRequest request) {
        Map<String, String> respMap = new HashMap<>();
        //设置文件保存路径
        String path = "C:\\wxpicture\\";
        File dir = new File(path);
        if (!dir.exists()) {
            //路径不存在侧创建
            dir.mkdir();
        }
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpServletRequest.getFile("pic");
        if (!file.isEmpty()) {
            try {
                String pictureName = UUID.randomUUID().toString() + ".jpg";//保存的文件名
                String destPath = path + pictureName;
                byte[] bytes = file.getBytes();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(destPath)));
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.close();
                boolean uploadPermission = BaiduUtils.checkPornograp(destPath);//图片检测//色情、政治检测
                if (uploadPermission) {
                    //图片审核通过
                    if (multipartHttpServletRequest.getParameter("photoId") == null) {
                        System.out.println("第一次保存");
                        //相册第一次保存
                        String introduce = multipartHttpServletRequest.getParameter("introduce");
                        String userId = (String) redisTemplate.opsForValue().get(multipartHttpServletRequest.getParameter("thirdSessionKey"));
                        Map<String, String> saveMap = new HashMap<>();
                        saveMap.put("photoId", UUID.randomUUID().toString());
                        saveMap.put("ownerId", userId);
                        saveMap.put("instruction", introduce);
                        if (request.getParameter("location") != null) {
                            saveMap.put("location", request.getParameter("location"));
                        }
                        saveMap.put("photoURL", pictureName);//图片url
                        boolean isSave = userService.firstSave(saveMap);
                        if (isSave) {
                            respMap.put("photoId", saveMap.get("photoId"));
                            //System.out.println(respMap);
                            return respMap;
                        } else {
                            respMap.put("status", "-1");
                            return respMap;
                        }
                    } else {
                        System.out.println("第二次保存");
                        //同一相册继续加载
                        Map<String, String> saveMap2 = new HashMap<>();
                        System.out.println("photoId:"+request.getParameter("photoId"));
                        saveMap2.put("photoId", request.getParameter("photoId"));
                        saveMap2.put("photoURL", ',' + pictureName);//图片url
                        //更新相册信息
                        System.out.println(saveMap2);
                        boolean isUpdate = userService.nextSave(saveMap2);
                        System.out.println("next保存："+isUpdate);
                        if (isUpdate) {
                            respMap.put("status", "1");
                            return respMap;
                        } else {
                            respMap.put("status", "-1");
                            return respMap;        //保存错误
                        }

                    }
                    //保存结束
                } else {
                    File file2 = new File(destPath);
                    if (file2.exists() && file2.isFile())
                        file2.delete();//删除图片
                    respMap.put("status", "-1");
                    return respMap;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("file is null");
        respMap.put("status", "-1");
        return respMap;
    }
}
