package com.cust.dao;

import com.cust.Entity.Photocomment;

public interface PhotocommentMapper {
    int deleteByPrimaryKey(String id);

    int insert(Photocomment record);

    int insertSelective(Photocomment record);

    Photocomment selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Photocomment record);

    int updateByPrimaryKey(Photocomment record);
}