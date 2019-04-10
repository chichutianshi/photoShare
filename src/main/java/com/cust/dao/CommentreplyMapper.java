package com.cust.dao;

import com.cust.Entity.Commentreply;

public interface CommentreplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Commentreply record);

    int insertSelective(Commentreply record);

    Commentreply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commentreply record);

    int updateByPrimaryKey(Commentreply record);
}