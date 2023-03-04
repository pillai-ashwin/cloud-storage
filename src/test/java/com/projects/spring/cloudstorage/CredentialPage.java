package com.projects.spring.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CredentialPage {
    private WebDriver webDriver;

    private final String credTabId="nav-credentials-tab";
    private final String addCredBtnId="add-cred-button";
    private final String credUrlId="credential-url";
    private final String credUserId="credential-username";
    private final String credPasswdId="credential-password";
    private final String credSubmitBtnId="credentialSubmit";
    private final String credUrlTextId="cred-url-display";
    private final String credUsernameTextId="cred-username-display";
    private final String credPasswordTextId="cred-password-display";
    private final String editCredBtnId="cred-edit-button";
    private final String deleteCredBtnId="cred-delete-button";

    public CredentialPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver,this);
    }

    @FindBy(id=credTabId)
    WebElement credTab;
    @FindBy(id=addCredBtnId)
    WebElement addCredBtn;
    @FindBy(id=credUrlId)
    WebElement credUrl;
    @FindBy(id=credUserId)
    WebElement credUser;
    @FindBy(id=credPasswdId)
    WebElement credPasswd;
    @FindBy(id=credSubmitBtnId)
    WebElement credSubmitBtn;

    public String getCredTabId() { return credTabId; }
    public String getAddCredBtnId() { return addCredBtnId;}

    public void clickCredTab(){credTab.click();}
    public void clickAddCredBtn(){addCredBtn.click();}
    public void inputUrl(String url){
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='"+url+"';",this.credUrl);
    }
    public void inputUserName(String uname){
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='"+uname+"';",this.credUser);
    }
    public void inputPasswd(String pw){
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='"+pw+"';",this.credPasswd);
    }
    public String getPasswdInModal(){
        return credPasswd.getAttribute("value");
    }

    public void clickCredSubmitBtn(){
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",this.credSubmitBtn);
    }

    public String getUrl(int pos){
        List<WebElement> urls=webDriver.findElements(By.id(credUrlTextId));
        return urls.get(pos).getAttribute("innerHTML");
    }
    public String getUname(int pos){
        List<WebElement> unames=webDriver.findElements(By.id(credUsernameTextId));
        return unames.get(pos).getAttribute("innerHTML");
    }
    public String getPw(int pos){
        List<WebElement> pwds=webDriver.findElements(By.id(credPasswordTextId));
        return pwds.get(pos).getAttribute("innerHTML");
    }

    public void clickEditCredBtn(int pos){
        List<WebElement> edbtns=webDriver.findElements(By.id(editCredBtnId));
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",edbtns.get(pos));
    }

    public void clickDeleteCredBtn(int pos){
        List<WebElement> delbtns=webDriver.findElements(By.id(deleteCredBtnId));
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",delbtns.get(pos));
    }

    public List<WebElement> getEditBtns(){ return webDriver.findElements(By.id(editCredBtnId));}

}