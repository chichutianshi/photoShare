package com.cust.dao;

import com.cust.Entity.Allphotos;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AllphotosMapper {
    int deleteByPrimaryKey(String photoid);

    @Insert("insert into allphotos(photoId,ownerId,instruction,location,photoURL,createTime,categories)" +
            " values(#{photoId},#{ownerId},#{instruction},#{location},#{photoURL},#{createTime},{categories})")
    int firstInsert(Map record);

    @Update("update allphotos set photoURL=CONCAT(photoURL,#{photoURL}) where photoId=#{photoId}")
    int nextInsert(Map record);

    int insertSelective(Allphotos record);

    Allphotos selectByPrimaryKey(String photoid);

    int updateByPrimaryKeySelective(Allphotos record);

    int updateByPrimaryKey(Allphotos record);

    @Select("select ownerId,instruction,location,photoURL,likeNum,categories from allphotos")
    Allphotos randomSelect();
}