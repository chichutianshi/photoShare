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
        if (list != null) {
            System.out.println(list);
            for (Map aList : list) {
                aList.put("show", "false");
            }
            return list;
        }
        System.out.println("空");
        return null;
    }
}
