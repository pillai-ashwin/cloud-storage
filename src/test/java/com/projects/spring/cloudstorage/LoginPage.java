package com.projects.spring.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id="inputUsername")
    private WebElement username;

    @FindBy(id="inputPassword")
    private WebElement password;

    @FindBy(id="login-button")
    private WebElement loginBtn;

    @FindBy(id="signup-success-msg")
    private WebElement signupSuccessMsg;


    public LoginPage(WebDriver webDriver) {PageFactory.initElements(webDriver, this);}

    public void LoginNow(String uname,String passw){
        username.clear();
        username.sendKeys(uname);
        password.clear();
        password.sendKeys(passw);
        loginBtn.click();
    }

    public boolean signupOkMsgDisplayed() { return signupSuccessMsg.isDisplayed(); }


}