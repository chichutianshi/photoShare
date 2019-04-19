package com.cust.dao;

import com.cust.Entity.Allphotos;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AllphotosMapper {

    @Insert("insert into allphotos(photoId,ownerId,instruction,location,photoURL,createTime,categories)" +
            " values(#{photoId},#{ownerId},#{instruction},#{location},#{photoURL},#{createTime},{categories})")
    int firstInsert(Map record);

    @Update("update allphotos set photoURL=CONCAT(photoURL,#{photoURL}) where photoId=#{photoId}")
    int nextInsert(Map record);

    @Select("select * from allphotos order by likeNum DESC,createTime DESC")
    List<Allphotos> randomSelect();
}