package com.projects.spring.cloudstorage.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.projects.spring.cloudstorage.models.File;

@Mapper
public interface FileMapper {
    @Delete("Delete from files where fileId = #{fileId}")
    int deleteFile(int fileId);

    @Insert("Insert into files (filename, contentType, fileSize, fileData, userId) VALUES (#{filename}, #{contentType}, #{fileSize}, #{fileData}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Select("Select * from files where userId = #{userId}")
    List<File> getFilesByUser(int userId);

    @Select("Select * from files where fileId = #{fileId}")
    File getFileById(int fileId);
    
}
