package com.cust.service;

import com.cust.dao.AllphotosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSearchService {
    @Autowired
    public AllphotosMapper allphotosMapper;

}
