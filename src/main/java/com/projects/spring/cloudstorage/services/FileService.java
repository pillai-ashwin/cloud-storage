package com.projects.spring.cloudstorage.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projects.spring.cloudstorage.mappers.FileMapper;
import com.projects.spring.cloudstorage.models.File;

@Service
public class FileService {
    private final UserService userService;
    private final FileMapper fileMapper;


    public FileService(UserService userService, FileMapper fileMapper) {
        this.userService = userService;
        this.fileMapper = fileMapper;
    }

    public List<File> getFilesByUser(int userId) {
        return fileMapper.getFilesByUser(userId);
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public boolean addFile(MultipartFile multipartFile, String username) throws IOException {
        int userId = userService.getUser(username).getUserId();

        File file = new File(
                null,
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                Long.toString(multipartFile.getSize()),
                multipartFile.getBytes(),
                userId
            );
        return fileMapper.insertFile(file) > 0;
    }

    public boolean deleteFile(int fileId) {
        return fileMapper.deleteFile(fileId) > 0;
    }
}
