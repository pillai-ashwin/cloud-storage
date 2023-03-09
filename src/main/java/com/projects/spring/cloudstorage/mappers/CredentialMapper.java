package com.projects.spring.cloudstorage.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.projects.spring.cloudstorage.models.Credential;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{id}")
    Credential findCredentialById(int id);

    @Select("SELECT * FROM CREDENTIALS WHERE url = #{url}")
    Credential findCredentialByUrl(String url);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userId) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int createCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE userId = #{userId} AND credentialId = #{credentialId}")
    int updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    int deleteCredentialById(int credentialId);

    @Select("SELECT * FROM CREDENTIALS")
    List<Credential> findAllCredentials();

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    List<Credential> findCredentialByUser(int userId);

    @Select("Select key FROM CREDENTIALS WHERE credentialId=#{credentialId}")
    String getKey(Integer credentialId);
}
