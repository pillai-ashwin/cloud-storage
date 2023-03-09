package com.projects.spring.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.projects.spring.cloudstorage.services.CredentialService;
import com.projects.spring.cloudstorage.services.EncryptionService;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrl;
	// private final String firstName="Ashwin";
	// private final String lastName="Pillai";
	// private final String username="ashwinpillai";
	// private final String password="ashwinpillai-cloudstorage";

	@Autowired
	private EncryptionService encryptionService;
	@Autowired
	private CredentialService credentialService;

	private static WebDriver driver;
	private Logger logger= LoggerFactory.getLogger(CloudStorageApplicationTests.class);

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
	}

	@AfterAll
	 public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeEach
	public void beforeEach() throws InterruptedException {
		// driver = new ChromeDriver();
		baseUrl="http://localhost:" + this.port;
		sleep(2000);

	}
	// Part 1 - Test signup and login flow
	@Test
	@Order(1)
	/* 
	 * Verifies that an unauthorized user -
	 * 1) can only access the login and signup pages.
	 * 2) cannot access the home page.
	*/
	public void getLoginPage() throws InterruptedException {
		logger.error("test 1 -accessibility and security");

		driver.get(baseUrl + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		sleep(2000);


		driver.get(baseUrl+"/signup");
		Assertions.assertEquals("Sign Up",driver.getTitle());
		sleep(2000);

		driver.get(baseUrl+"/home");
        Assertions.assertNotEquals("Home",driver.getTitle());
	}

	@Test
	@Order(2)
	/*
	 * Verify - 
	 * 1) Sign up a new user.
	 * 2) After successful sign up user is redirected to login page.
	 */
	private void doMockSignUp(String firstName, String lastName, String userName, String password) throws InterruptedException{
		logger.error("Test 2 - Signup & check for login page redirect");
		driver.get(baseUrl + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signUpNow(firstName, lastName, userName, password);
		//now we should have been redirected to login page with successful msg
		//driver.navigate().to(baseUrl +"/login");
		//driver.switchTo().window("/login");
		sleep(4000);
		assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(3)
	/*
	 * Verify -
	 * 1) Log in and verify that the home page is accessible,
	 * 2) Log out and verify that the home page is no longer accessible
	 */
	private void doLogIn(String userName, String password) throws InterruptedException {
		logger.error("Test 3 - Check home page accessibility after login and logout");
        driver.get(baseUrl + "/login");
        LoginPage loginPage=new LoginPage(driver);
        loginPage.LoginNow(userName, password);
		sleep(1000);
		//Assert page redirected to home so login is successful
		Assertions.assertEquals("Home",driver.getTitle());

		driver.get(baseUrl + "/home");
		HomePage homePage=new HomePage(driver);
		homePage.clickLogoutBtn();
		sleep(1000);
		//Assert home page is not accessible after logout
		Assertions.assertNotEquals("Home",driver.getTitle());

		//now it is on login page actually, login again for other tests to continue
		driver.get(baseUrl + "/login");
		loginPage.LoginNow(userName, password);
		sleep(1000);
		//Assert page redirected to home so login is successful
		Assertions.assertEquals("Home",driver.getTitle());

	}
	
	@Test
	@Order(4)
	public void testRedirection() throws InterruptedException {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	@Test
	@Order(5)
	public void testBadUrl() throws InterruptedException {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	@Test
	@Order(6)
	public void testLargeUpload() throws InterruptedException {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("file-upload-button"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	@Order(7)
	public void testNotes() throws Exception{
		logger.error("Test 4 - Notes");
		// Create a test account
		doMockSignUp("Note Feature","Test","NFT","123");
		doLogIn("NFT", "123");

		String noteTitle_org = "Note Title Create Test";
		String noteDes_org = "Note Description Create Test";
		String noteTitle_upt = "Note Title Update Test";
		String noteDes_upt = "Note Description Update Test";
		driver.get(baseUrl+"/home");
		NotePage notePage=new NotePage(driver);

		waitForVisibility(notePage.getNoteTabId());
		notePage.clickNoteTab();

		waitForVisibility(notePage.getAddNoteBtnId());
		notePage.clickAddNoteBtn();

		//Create New Note and verify//
		//now the Modal is there, input values
		//waitForVisibility(notePage.getNoteSubmitBtnId());
		notePage.inputNoteTitle(noteTitle_org);
		notePage.inputNoteDescription(noteDes_org);
		sleep(2000);
		notePage.submitNote();
		//go back to noteTab
		sleep(2000);
		// try to create note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
			driver.get(baseUrl+"/home");
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Note creation failed");
		}
		sleep(2000);
		notePage.clickNoteTab();
		waitForVisibility(notePage.getNoteTitleDisplayId());
        //verify new note is added and displayed as expected
		Assertions.assertEquals(notePage.getNoteTitleDisplay(),noteTitle_org);
		Assertions.assertEquals(notePage.getNoteDesDisplay(),noteDes_org);

		sleep(3000);

		//Edit the newly created Note and verify//
		notePage.clickNoteEditBtn();
		sleep(1000);
		//waitForVisibility(notePage.getNoteSubmitBtnId());
		//edit the note
		notePage.inputNoteTitle(noteTitle_upt);
		notePage.inputNoteDescription(noteDes_upt);
		sleep(2000);
		notePage.submitNote();

		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
			driver.get(baseUrl+"/home");
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Note update failed");
		}

		//go back to noteTab
		sleep(2000);
		notePage.clickNoteTab();
		waitForVisibility(notePage.getNoteTabId());
		//verify  note is edited and displayed as expected
		Assertions.assertEquals(notePage.getNoteTitleDisplay(),noteTitle_upt);
		Assertions.assertEquals(notePage.getNoteDesDisplay(),noteDes_upt);

		//Delete the newly edited Note and verify//
		//go back to notTab
		waitForVisibility(notePage.getNoteTabId());
		notePage.clickNoteTab();
		//delete the Note and verify it is not there
		notePage.clickNoteDeleteBtn();
		//Assertions.assertNull(notePage.getNoteTitleDisplay());
		//Assertions.assertThrows(Exception.class,null);

		sleep(2000);
		//go back to noteTab, visually see note is deleted and let's assert it
		driver.get(baseUrl+"/home");
		waitForVisibility(notePage.getNoteTabId());
		notePage.clickNoteTab();
		sleep(1000);
		Assertions.assertEquals(0,notePage.getNoteEditBtns().size());

	}

	@Test
	@Order(8)
	public void testCredentials() throws Exception {
		logger.error("Test 5 - Credentials");
		// Create a test account
		doMockSignUp("Credentials Feature","Test","CFT","123");
		doLogIn("CFT", "123");
		
		driver.get(baseUrl + "/home");
		CredentialPage credentialPage = new CredentialPage(driver);

		//Credentials
		String[] urls_org= new String[]{"foo.com", "abc.com", "xyz.com"};
		String[] usernames_org = new String[]{"naruto","sasuke","sakura"};
		String[] passwords_org = new String[]{"rasengan","chidori","chaa"};
		String[] urls_upt= new String[]{"sagemode.com", "susanoo.com", "healing.com"};
		String[] usernames_upt = new String[]{"uzumaki","uchiha","haruki"};
		String[] passwords_upt = new String[]{"hokage","jonin","chunin"};

		//Create new credentials
		int total=3;
		for(int pos=0;pos<total;pos++){
			//wait for credential page is visible
			waitForVisibility(credentialPage.getCredTabId());
			credentialPage.clickCredTab();
			sleep(1000);
			//click add new credential button
			waitForVisibility(credentialPage.getAddCredBtnId());
			credentialPage.clickAddCredBtn();
			//now the modal is there, input values
			credentialPage.inputUrl(urls_org[pos]);
			credentialPage.inputUserName(usernames_org[pos]);
			credentialPage.inputPasswd(passwords_org[pos]);
			sleep(2000);
			credentialPage.clickCredSubmitBtn();
			WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
			try {
				webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
				driver.get(baseUrl+"/home");
			} catch (org.openqa.selenium.TimeoutException e) {
				logger.error("Credential creation failed");
			}
		}

		waitForVisibility(credentialPage.getCredTabId());
		credentialPage.clickCredTab();
		sleep(1000);

		//Verify credentials created
		for(int pos=0; pos<total; pos++) {
			String displayedUrl = credentialPage.getUrl(pos);
			String displayedUname = credentialPage.getUname(pos);	
			String displayedPwd = credentialPage.getPw(pos);
			//decrypt password
			String key = credentialService.getKeyById(pos+1);
			displayedPwd= encryptionService.decryptValue(displayedPwd,key);

			Assertions.assertEquals(displayedUrl,urls_org[pos]);
			Assertions.assertEquals(displayedUname,usernames_org[pos]);
            Assertions.assertEquals(displayedPwd,passwords_org[pos]);
		}

		//Edit Credentials and verify if the updated results match the updated values
		for(int pos=0;pos<total;pos++) {
			//wait for Credential page is visible
			waitForVisibility(credentialPage.getCredTabId());
			credentialPage.clickCredTab();
			sleep(1000);
			credentialPage.clickEditCredBtn(pos);
			sleep(1000);
			credentialPage.inputUrl(urls_upt[pos]);
			credentialPage.inputUserName(usernames_upt[pos]);
			//before update pwd, first verify it is decrypted
			Assertions.assertNotEquals(credentialPage.getPasswdInModal(),passwords_org[pos]);
			credentialPage.inputPasswd(passwords_upt[pos]);
			sleep(2000);
			credentialPage.clickCredSubmitBtn();
			WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
			try {
				webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
				driver.get(baseUrl+"/home");
			} catch (org.openqa.selenium.TimeoutException e) {
				logger.error("Credential update failed");
			}
		}

		// Verify deleting credentials
		for(int pos=0;pos<total;pos++){
			waitForVisibility(credentialPage.getCredTabId());
			credentialPage.clickCredTab();
			sleep(1000);

            //After each deletion, the next one to delete is always at position 0
			credentialPage.clickDeleteCredBtn(0);
			WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
			try {
				webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
				driver.get(baseUrl+"/home");
			} catch (org.openqa.selenium.TimeoutException e) {
				logger.error("Credential update failed");
			}
		}

		//verify that all credentials are deleted
		waitForVisibility(credentialPage.getCredTabId());
		credentialPage.clickCredTab();
		Assertions.assertEquals(0,credentialPage.getEditBtns().size());
		sleep(2000);

	}

	private void waitForVisibility(String id) {
		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
	}
}
