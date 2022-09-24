package com.projects.spring.cloudstorage.models;

public class Note {
    private Integer noteId;
    String noteTitle;
    String noteDescription;
    int userId;
    
    public Note(Integer noteId, String noteTitle, String noteDescription, int userId) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.userId = userId;
    }
    public Integer getNoteId() {
        return noteId;
    }
    public String getNoteTitle() {
        return noteTitle;
    }
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
    public String getNoteDescription() {
        return noteDescription;
    }
    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}