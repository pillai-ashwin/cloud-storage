package com.projects.spring.cloudstorage.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.projects.spring.cloudstorage.models.User;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstName, lastName) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int createUser(User user);

    @Update("UPDATE USERS SET username = #{username}, salt = #{salt}, password = #{password}, firstName = #{firstName}, lastName= #{lastName} WHERE userId = #{userId}")
    int updateUser(User user);

    @Delete("DELETE * FROM USERS WHERE userId = #{userId}")
    int deleteUser(int userid);

    @Select("SELECT * FROM USERS WHERE userId = #{id}")
    User getUserById(int id);
}
