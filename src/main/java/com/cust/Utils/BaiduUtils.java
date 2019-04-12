package com.cust.Utils;

import com.baidu.aip.imagecensor.AipImageCensor;
import org.json.JSONObject;

import java.io.File;

public class BaiduUtils {
    //设置APPID/AK/SK
    private static final String APP_ID = "16007168";
    private static final String API_KEY = "bLrW0YLOM6S0t0iC5uStDId0";
    private static final String SECRET_KEY = "MYY8rp7Cyr26cxVBcZlWbWCEcNRP7Bqn";

    // 初始化一个AipImageCensor
    static AipImageCensor client=new AipImageCensor(APP_ID, API_KEY, SECRET_KEY);

    public static boolean checkPornograp(String path) {
        JSONObject res = client.antiPorn(path);
        System.out.println(res.get("conclusion"));
        if ("色情"==res.get("conclusion")){
            return false;
        }else
            return true;
    }

}
