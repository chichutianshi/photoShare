package com.cust.dao;

import com.cust.Entity.Allphotos;

public interface AllphotosMapper {
    int deleteByPrimaryKey(String photoid);

    int insert(Allphotos record);

    int insertSelective(Allphotos record);

    Allphotos selectByPrimaryKey(String photoid);

    int updateByPrimaryKeySelective(Allphotos record);

    int updateByPrimaryKey(Allphotos record);
}