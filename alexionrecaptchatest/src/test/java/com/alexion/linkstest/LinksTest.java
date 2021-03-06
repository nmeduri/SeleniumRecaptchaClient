package com.alexion.linkstest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class LinksTest {
	WebDriver driver;
	String url = "https://alexion.com/";

	@Test(priority = 1)
	public void medicalInformation() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");

		ChromeDriver driver = new ChromeDriver();

		// TODO this is used to change the location of chrome browser opened by selenium
		driver.manage().window().setPosition(new Point(900, 20));
		driver.get(url);
		driver.manage().window().maximize();

		driver.findElement(By.linkText("Contact Us")).click();

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		driver.findElement(By.linkText("Medical Information")).click();
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_1__Value\"]"))
				.sendKeys("Naveen");

		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_2__Value\"]"))
				.sendKeys("Kumar");

		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_3__Value\"]"))
				.sendKeys("naveen.meduri@gmail.com");

		Select sel = new Select(driver.findElement(
				By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_11__Value\"]")));
		sel.selectByVisibleText("India");

		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_14__Value\"]"))
				.sendKeys("Testing");

		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_15__Value\"]"))
				.sendKeys("ABCD");

		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_16__Value\"]"))
				.sendKeys("XYZ");
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		WebElement captchaKeyDivElement = driver.findElement(By.className("g-recaptcha"));
		// TODO 3) get site key from source code of target website
		String siteKey = captchaKeyDivElement.getAttribute("data-sitekey");
		System.out.println("Site Key : " + siteKey);
		String captchaSolution = getCaptchaSolution(siteKey, url);
		// fill that response in text area
		// WebElement textArea =
		// driver.findElementByCssSelector("#g-recaptcha-response");
		WebElement textArea = driver.findElement(By.cssSelector("#g-recaptcha-response"));
		String js = "arguments[0].style.height='auto'; arguments[0].style.display='block';";
		((JavascriptExecutor) driver).executeScript(js, textArea);
		textArea.clear();
		textArea.sendKeys(captchaSolution);
		// submit the page
		captchaKeyDivElement.submit();
		Thread.sleep(2000);
	}

	@Test(priority = 2)
	public void careerInquiries() {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
        // TODO this is used to change the location of chrome browser opened by selenium
		driver.manage().window().setPosition(new Point(900, 20));
		driver.manage().window().maximize();
		driver.get(url);
		driver.findElement(By.linkText("Contact Us")).click();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.findElement(By.linkText("Career Inquiries")).click();
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_0__Value\"]"))
				.sendKeys("Alexander");
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_1__Value\"]"))
				.sendKeys("Fleming");
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_2__Value\"]"))
				.sendKeys("abc@bcd.com");
		WebElement captchaKeyDivElement = driver.findElement(By.className("g-recaptcha")); 
		// TODO 3) get site key from
		// source code of target website 
		String siteKey =captchaKeyDivElement.getAttribute("data-sitekey");
		System.out.println("Site Key : " + siteKey);
		String captchaSolution = getCaptchaSolution(siteKey, url); // fill that response in text area
		WebElement textArea1 = driver.findElement(By.cssSelector("#g-recaptcha-response"));
		String js = "arguments[0].style.height='auto'; arguments[0].style.display='block';";
		((JavascriptExecutor) driver).executeScript(js, textArea1);
		textArea1.clear();
		textArea1.sendKeys(captchaSolution); // submit the page
		captchaKeyDivElement.submit();

	}

	private String getCaptchaSolution(String siteKey, String url) {
		try {
			// TODO 2) captcha solver server address. You can use your local IP address
			// where captcha-server is running.
			String captchaServerUrl = "http://localhost:9990";
			RestTemplate template = new RestTemplate();
			Map<String, String> map = new HashMap<>();
			map.put("host", url);
			map.put("siteKey", siteKey);
			ResponseEntity<CaptchaResponse> response = template.postForEntity(captchaServerUrl + "/solve", map,
					CaptchaResponse.class);
			if (response.getStatusCode().is2xxSuccessful()) {
				CaptchaResponse captchaResponse = response.getBody();
				int id = captchaResponse.getRequestId();
				while (true) {
					ResponseEntity<CaptchaResponse> solutionResponse = template
							.getForEntity(captchaServerUrl + "/solution/" + id, CaptchaResponse.class);
					if (solutionResponse.getStatusCode().is2xxSuccessful()) {
						String solution = solutionResponse.getBody().getSolution();
						if (solution != null && !solution.isEmpty()) {
							return solution;
						}
					}
					Thread.sleep(5 * 1000);
				}
			} else {
				System.err.println("Error " + response);
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return null;
	}
}
