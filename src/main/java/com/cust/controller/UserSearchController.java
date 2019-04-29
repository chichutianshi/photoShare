package com.cust.controller;


import com.cust.service.UserSearchService;
import easy.web.RequestTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserSearchController {
    @Autowired
    private UserSearchService userSearchService;

    @RequestMapping("/loadPhotos")
    public List<Map> showPhotos(HttpServletRequest httpServletRequest) {
        Map reqMap = RequestTool.getParameterMap(httpServletRequest);
        String thirdSessionKey = (String) reqMap.get("thirdSessionKey");
        //如果reqMap中含有thirdSessionKey进行个性化推荐显示
        System.out.println(thirdSessionKey);
        if (thirdSessionKey.equals("")) {
            return userSearchService.selectAllPhotos(Integer.valueOf((String)(reqMap.get("selectRow"))));
        } else {
            System.out.println("asd");
        }
        return null;
    }

}