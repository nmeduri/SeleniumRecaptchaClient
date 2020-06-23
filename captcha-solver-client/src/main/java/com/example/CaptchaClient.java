package com.example;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CaptchaClient {

	public static void main(String[] args) {
		CaptchaClient.testBySeleniumMySite();
	}

	public static void testBySeleniumMySite() {
		System.setProperty("webdriver.chrome.driver", "C:/SeleniumSoftware/drivers/chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		// TODO this is used to change the location of chrome browser opened by selenium
		driver.manage().window().setPosition(new Point(900, 20));
		// TODO 1) this is the url which you want to automate and having recaptcha on it
		String url = "https://alexion.com/";
		driver.manage().window().maximize();
		driver.get(url);

		// Step 1
		driver.findElement(By.linkText("Contact Us")).click();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		/*// Step 2
		driver.findElement(By.linkText("Medical Information")).click();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_1__Value\"]")).sendKeys("Naveen");
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_2__Value\"]")).sendKeys("Kumar");
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_3__Value\"]")).sendKeys("naveen.meduri@gmail.com");
		Select sel=new Select(driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_11__Value\"]")));
		sel.selectByVisibleText("India");
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_14__Value\"]")).sendKeys("Testing");
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_15__Value\"]")).sendKeys("ABCD");
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_16__Value\"]")).sendKeys("XYZ");*/
		
		driver.findElement(By.linkText("Career Inquiries")).click();
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_0__Value\"]")).sendKeys("Alexander");
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_1__Value\"]")).sendKeys("Fleming");
		driver.findElement(By.xpath("//*[@id=\"wffm77b644cfd63c4ae28f2cdb6a41c929cd_Sections_0__Fields_2__Value\"]")).sendKeys("abc@bcd.com");
		
		
		WebElement captchaKeyDivElement = driver.findElement(By.className("g-recaptcha"));
		// TODO 3) get site key from source code of target website
		String siteKey = captchaKeyDivElement.getAttribute("data-sitekey");
		System.out.println("Site Key : " + siteKey);
		String captchaSolution = getCaptchaSolution(siteKey, url);
		// fill that response in text area
		WebElement textArea = driver.findElementByCssSelector("#g-recaptcha-response");
		String js = "arguments[0].style.height='auto'; arguments[0].style.display='block';";
		((JavascriptExecutor) driver).executeScript(js, textArea);
		textArea.clear();
		textArea.sendKeys(captchaSolution);
		// submit the page
		captchaKeyDivElement.submit();
	}

	private static String getCaptchaSolution(String siteKey, String url) {
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
