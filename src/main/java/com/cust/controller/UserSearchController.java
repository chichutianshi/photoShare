package com.cust.controller;


import easy.web.RequestTool;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserSearchController {

    @RequestMapping("/loadPhotos")
    public Map showPhotos(HttpServletRequest httpServletRequest) {
        Map map = RequestTool.getParameterMap(httpServletRequest);
        System.out.println(map);
        return null;
    }
}
