package com.cust.Utils;

import easy.security.MD5;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;

@Component
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

    public  boolean delete(String path){
        File file = new File(path);
        if(!file.exists()){
            return false;
        }
        if(file.isFile()){
            return file.delete();
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if(f.isFile()){
                if(!f.delete()){
                    System.out.println(f.getAbsolutePath()+" delete error!");
                    return false;
                }
            }else{
                if(!this.delete(f.getAbsolutePath())){
                    return false;
                }
            }
        }
        return file.delete();
    }

}