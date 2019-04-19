package com.cust.controller;


import com.cust.service.UserSearchService;
import easy.web.RequestTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class UserSearchController {
    @Autowired
    private UserSearchService userSearchService;

    @RequestMapping("/loadPhotos")
    public List showPhotos(HttpServletRequest httpServletRequest) {
        Map reqMap = RequestTool.getParameterMap(httpServletRequest);
        //如果reqMap中含有thirdSessionKey进行个性化推荐显示
        if (reqMap.get("thirdSessionKey") != "") {

        } else {
            return userSearchService.selectAllPhotos();
        }
        return null;
    }


}
