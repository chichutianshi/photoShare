package com.cust.Utils;

import easy.security.MD5;
import java.io.File;
import java.util.UUID;

public class Token {

    public final static String CreateToken() {
        String uid = UUID.randomUUID().toString();
        MD5 m = new MD5();
        String token = m.calcMD5(uid);
        return token;
    }

    public static String createNewUserId() {

        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        String newId = uuid + System.currentTimeMillis();
        return newId;
    }

    /**
     * 迭代删除文件夹
     * @param dirPath 文件夹路径
     */
    public static void deleteDir(File path) {
        if (null != path) {
            if (!path.exists())
                return;
            if (path.isFile()) {
                boolean result = path.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc(); // 回收资源
                    result = path.delete();
                }
            }
            File[] files = path.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i]);
                }
            }
            path.delete();
        }
    }



}