package com.projects.spring.cloudstorage.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projects.spring.cloudstorage.services.FileService;
import com.projects.spring.cloudstorage.services.NoteService;
import com.projects.spring.cloudstorage.services.UserService;

@Controller
@RequestMapping("/home")
public class HomeController {
    UserService userService;
    FileService fileService;
    NoteService noteService;


    public HomeController(UserService userService, FileService fileService, NoteService noteService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
    }

    @GetMapping()
    public String homeView(Authentication authentication, Model model) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("files", fileService.getFilesByUser(userId));
        model.addAttribute("notes", noteService.getNoteByUser(userId));
        return "home";
    }
}