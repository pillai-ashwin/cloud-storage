package com.projects.spring.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class NotePage {
    private WebDriver webDriver;

    private final String noteTabId="nav-notes-tab";
    private final String addNoteBtnId="add-note-button";
    private final String noteTitleId="note-title";
    private final String noteDescriptionId="note-description";
    private final String noteSubmitBtnId="noteSubmit";
    private final String noteTitleDisplayId="note-title-display";
    private final String noteDesDisplayId="note-description-display";
    private final String noteEditBtnId="note-edit-button";
    private final String noteDeleteBtnId="note-delete-button";

    public String getNoteTabId() {
        return noteTabId;
    }
    public String getAddNoteBtnId() {
        return addNoteBtnId;
    }
    public String getNoteTitleId() {
        return noteTitleId;
    }
    public String getNoteDescriptionId() {
        return noteDescriptionId;
    }
    public String getNoteSubmitBtnId() {
        return noteSubmitBtnId;
    }
    public String getNoteTitleDisplayId() { return noteTitleDisplayId; }
    public String getNoteDesDisplayId() { return noteDesDisplayId; }
    public String getNoteEditBtnId() { return noteEditBtnId; }
    public String getNoteDeleteBtnId() { return noteDeleteBtnId; }

    @FindBy(id=noteTabId)
    private WebElement noteTab;

    @FindBy(id=addNoteBtnId)
    private WebElement addNoteBtn;

    @FindBy(id=noteTitleId)
    WebElement noteTitle;

    @FindBy(id=noteDescriptionId)
    WebElement noteDescription;

    @FindBy(id=noteSubmitBtnId)
    WebElement noteSubmitBtn;

    @FindBy(id=noteTitleDisplayId)
    WebElement noteTitleDisplay;

    @FindBy(id=noteDesDisplayId)
    WebElement noteDesDisplay;

    @FindBy(id=noteEditBtnId)
    WebElement noteEditBtn;
    @FindBy(id=noteDeleteBtnId)
    WebElement noteDeleteBtn;

    public NotePage(WebDriver webDriver) {
        this.webDriver=webDriver;
        PageFactory.initElements(webDriver,this);
    }

    public void clickNoteTab(){
        //noteTab.click();
        //WebDriverWait wait = new WebDriverWait(webDriver, 4000);
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(noteTabId)));
        //((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteTab);
        noteTab.click();
    }

    public void clickAddNoteBtn(){ addNoteBtn.click(); }

    public void inputNoteTitle(String title){
        //noteTitle.sendKeys(title);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + title + "';", this.noteTitle);
    }
    public void inputNoteDescription(String des) {
        //noteDescription.sendKeys(des);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + des + "';", this.noteDescription);
    }
    public void submitNote() {
        //noteSubmitBtn.click();
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteSubmitBtn);
    }

    public String getNoteTitleDisplay(){
        return noteTitleDisplay.getAttribute("innerHTML");
    }

    public String getNoteDesDisplay(){
        return noteDesDisplay.getAttribute("innerHTML");
    }

    public void clickNoteEditBtn() {
        //noteEditBtn.click();
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteEditBtn);
        }
    public void clickNoteDeleteBtn(){
        //noteDeleteBtn.click();
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", this.noteDeleteBtn);
    }

    public List<WebElement> getNoteEditBtns(){return webDriver.findElements(By.id(noteEditBtnId));}

}