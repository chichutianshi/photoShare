package com.cust.service;

import com.cust.Entity.Allphotos;
import com.cust.dao.AllphotosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSearchService {
    @Autowired
    public AllphotosMapper allphotosMapper;


    public List<Allphotos> selectAllPhotos() {
        List<Allphotos> list = allphotosMapper.randomSelect();
        if (list != null) {
            return list;
        }
        return null;
    }
}
