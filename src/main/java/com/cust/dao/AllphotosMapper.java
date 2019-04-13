package com.cust.dao;

import com.cust.Entity.Allphotos;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AllphotosMapper {
    int deleteByPrimaryKey(String photoid);

    @Insert("insert into allphotos(photoId,ownerId,instruction,location,photoURL)" +
            " values(#{photoId},#{ownerId},#{instruction},#{location},#{photoURL})")
    int firstInsert(Map record);

    @Update("update allphotos set photoURL=photoURL+#{photoURL} where photoId=#{photoId}")
    int nextInsert(Map record);

    int insertSelective(Allphotos record);

    Allphotos selectByPrimaryKey(String photoid);

    int updateByPrimaryKeySelective(Allphotos record);

    int updateByPrimaryKey(Allphotos record);
}