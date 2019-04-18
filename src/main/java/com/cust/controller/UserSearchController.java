package com.cust.controller;


import easy.web.RequestTool;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class UserSearchController {

    @RequestMapping("/loadPhotos")
    public Map showPhotos(HttpServletRequest httpServletRequest) {
        Map reqMap = RequestTool.getParameterMap(httpServletRequest);
        //如果reqMap中含有thirdSessionKey进行个性化推荐显示
        if (reqMap.get("thirdSessionKey") != "") {

        } else {

        }
        return null;
    }

}
