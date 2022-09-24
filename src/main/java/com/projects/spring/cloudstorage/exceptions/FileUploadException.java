package com.projects.spring.cloudstorage.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class FileUploadException {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleFileSizeLimitExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView result = new ModelAndView();
        result.setViewName("result");
        String status = "File is too large, file size limit is 10MB";
        result.addObject("errorMsg", true);
        result.addObject("message", status);
        return result;
    }
}
