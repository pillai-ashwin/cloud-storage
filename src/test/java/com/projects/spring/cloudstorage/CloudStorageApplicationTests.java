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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrl;
	private final String firstName="Ashwin";
	private final String lastName="Pillai";
	private final String username="ashwinpillai";
	private final String password="ashwinpillai-cloudstorage";

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
		signupPage.signUpNow(firstName, lastName, username, password);
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
        loginPage.LoginNow(userName,password);
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
		loginPage.LoginNow(username,password);
		sleep(1000);
		//Assert page redirected to home so login is successful
		Assertions.assertEquals("Home",driver.getTitle());

	}
	

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}
}
