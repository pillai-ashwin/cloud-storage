package com.projects.spring.cloudstorage.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.projects.spring.cloudstorage.models.User;

@Mapper
public interface UserMapper {
    
    @Select("select * from users where username= #{username}")
    User getUser(String username);

    @Insert("insert into users (username, salt, password, firstname, lastname) values(#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys=true, keyProperty="userid")
    int insert(User user);
}
