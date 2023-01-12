package com.projects.spring.cloudstorage.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.projects.spring.cloudstorage.models.File;
import com.projects.spring.cloudstorage.services.FileService;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ModelAndView fileUpload(@RequestParam("fileUpload") MultipartFile file, Authentication auth) {
        ModelAndView result = new ModelAndView();
        String status = null;

        try {
            if(file == null) throw new IOException("Choose a file to upload");
            if(fileService.addFile(file, auth.getName())) {
                result.addObject("success", true);
                status = "File uploaded successfully.";
            }
        } catch (IOException ex) {
            result.addObject("errorMsg", true);
            status = "Unable to upload file.";
        }
        result.setViewName("result");
        result.addObject("message", status);
        return result;
    }

    @GetMapping("/delete/{fileId}")
    public ModelAndView deleteFile(@PathVariable int fileId) {
        ModelAndView result = new ModelAndView();
        String status = "";
        
        if(fileService.deleteFile(fileId)) {
            result.addObject("success", true);
            status = "File deleted successfully.";
        } else {
            result.addObject("errorMsg", true);
            status = "Unable to delete file..";
        }
        result.setViewName("result");
        result.addObject("message", status);
        return result;
    }

    @GetMapping("/download/{fileId}")
    public void downloadFile(@PathVariable int fileId, HttpServletResponse response) throws IOException {
        File file = fileService.getFileById(fileId);

        response.setContentType(file.getContentType());
        response.setContentLength(Integer.parseInt(file.getFileSize()));
        String headerValue = "file:" + file.getFilename();
        response.setHeader("Content-Disposition", headerValue);
        response.getOutputStream().write(file.getFileData());
        response.flushBuffer();
    }
    
}
