package com.projects.spring.cloudstorage.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projects.spring.cloudstorage.models.Note;
import com.projects.spring.cloudstorage.services.NoteService;
import com.projects.spring.cloudstorage.services.UserService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public ModelAndView uploadNote(Authentication authentication, Note note, Model model) {
        ModelAndView result = new ModelAndView();
        String noteStatus = null;
        String username = authentication.getPrincipal().toString();
        note.setUserId(userService.getUser(username).getUserId());

        if(note.getNoteId() != null) {
            if(noteService.updateNote(note)) {
                result.addObject("success", true);
                noteStatus = "Note updated!";
            } else {
                result.addObject("errorMsg", true);
                noteStatus = "Unable to  update note, please try again";
            }
        } else {
            if(noteService.addNote(note)) {
                result.addObject("success", true);
                noteStatus = "Note added!";
            } else {
                result.addObject("errorMsg", true);
                noteStatus = "Unable to add note, please try again";
            }
        }
        result.setViewName("result");
        result.addObject("message", noteStatus);
        return result;
    }

    @GetMapping("/delete/{noteId}")
    public ModelAndView deleteNote(@PathVariable int noteId) {
        ModelAndView result = new ModelAndView();
        String noteStatus = null;

        boolean deleted = noteService.deleteNote(noteId);
        if(deleted) {
            result.addObject("success", true);
            noteStatus = "Note deleted!";
        } else {
            result.addObject("errorMsg", true);
            noteStatus = "Unable to delete note, please try again";
        }

        result.setViewName("result");
        result.addObject("message", noteStatus);
        return result;
    }
}
