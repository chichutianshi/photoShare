package com.cust.service;

import com.cust.Entity.Allphotos;
import com.cust.dao.AllphotosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserSearchService {
    @Autowired
    public AllphotosMapper allphotosMapper;


    public List<Map> selectAllPhotos(int selectRow) {
        List<Map> list = allphotosMapper.randomSelect(selectRow);
        String photoURL;
        String[] URL;
        if (list != null) {
            System.out.println(list);
            for (Map aList : list) {
                photoURL = (String) aList.get("photoURL");
                URL = photoURL.split(";");
                aList.put("photoURL", URL[0]);
            }
            return list;
        }
        System.out.println("空");
        return null;
    }
}
