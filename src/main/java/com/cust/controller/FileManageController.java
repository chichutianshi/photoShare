package com.cust.controller;

import com.cust.Utils.BaiduUtils;
import com.cust.service.UserService;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
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
    public void upPicture(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //设置文件保存路径
        String path = "C:\\wxpicture\\";
        File dir = new File(path);
        if (!dir.exists()) {
            //路径不存在侧创建
            dir.mkdir();
        }
        request.setCharacterEncoding("utf-8");  //设置编码
        //获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //如果没以下两行设置的话,上传大的文件会占用很多内存，
        //设置暂时存放的存储室,这个存储室可以和最终存储文件的目录不同
        /**
         * 原理: 它是先存到暂时存储室，然后再真正写到对应目录的硬盘上，
         * 按理来说当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的
         * 然后再将其真正写到对应目录的硬盘上
         */
        factory.setRepository(dir);
        //设置缓存的大小，当上传文件的容量超过该缓存时，直接放到暂时存储室
        factory.setSizeThreshold(1024 * 1024);
        //高水平的API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> list = upload.parseRequest((RequestContext) request);
            FileItem picture = null;
            for (FileItem item : list) {
                //非文本信息即文件
                if (!item.isFormField()) {
                    picture = item;
                }
            }

            String pictureName = UUID.randomUUID().toString()+".jpg";//保存的文件名
            String destPath = path + pictureName;
            //真正写到磁盘上
            File file = new File(destPath);
            OutputStream out = new FileOutputStream(file);
            InputStream in = picture.getInputStream();
            int length = 0;
            byte[] buf = new byte[1024];
            // in.read(buf) 每次读到的数据存放在buf 数组中
            while ((length = in.read(buf)) != -1) {
                //在buf数组中取出数据写到（输出流）磁盘上
                out.write(buf, 0, length);
            }
            in.close();
            out.close();
            boolean uploadPermission = BaiduUtils.checkPornograp(destPath);//图片检测//色情、政治检测
            PrintWriter printWriter = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            if (uploadPermission) {
                //图片审核通过
                if (request.getParameter("photoId") == null) {
                    //相册第一次保存
                    String introduce = request.getParameter("introduce");
                    String userId = (String) redisTemplate.opsForValue().get(request.getParameter("thirdSessionKey"));
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
                        Map<String, String> respMap = new HashMap<>();
                        respMap.put("photoId", saveMap.get("photoId"));
                        String json = new JSONObject(respMap).toString();
                        printWriter.write(json);
                    } else {
                        printWriter.write(-1);//保存错误
                    }
                    printWriter.flush();
                } else {
                    //同一相册继续加载
                    Map<String, String> saveMap = new HashMap<>();
                    saveMap.put("photoId", request.getParameter("photoId"));
                    saveMap.put("photoURL", "," + pictureName);//图片url
                    //更新相册信息
                    boolean isUpdate = userService.nextSave(saveMap);
                    if (isUpdate) {
                        Map<String, String> respMap = new HashMap<>();
                        respMap.put("photoId", saveMap.get("photoId"));
                        String json = new JSONObject(respMap).toString();
                        printWriter.write(json);
                    } else {
                        printWriter.write(-1);//保存错误
                    }
                    printWriter.flush();
                }
                //保存结束
            } else {
                File file2 = new File(destPath);
                if (file2.exists() && file2.isFile())
                    file2.delete();//删除图片
                printWriter.write(-1);
                printWriter.flush();
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
