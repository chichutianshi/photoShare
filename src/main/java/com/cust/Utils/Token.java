package com.cust.Utils;

import easy.security.MD5;

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
}