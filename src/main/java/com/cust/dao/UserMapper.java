package com.cust.dao;

import com.cust.Entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    @Select("select * from user where openid=#{openid}")
    User selectUser(String openid);

    @Insert("insert into user (id,nickname,gender,province,city,country,openid,avatarURL) values(#{id},#{nickname},#{gender},#{province},#{city},#{country},#{openid},#{avatarURL})")
    int insertUserInfo(User user);
}