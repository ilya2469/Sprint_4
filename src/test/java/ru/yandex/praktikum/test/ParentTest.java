package ru.yandex.praktikum.test;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import ru.yandex.praktikum.pageobject.MainPage;

import java.time.Duration;

public abstract class ParentTest {
    public WebDriver driver;
    public MainPage mainPage;

    @Before
    public void before() {
        String browser = System.getProperty("browser", "chrome");
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        switch (browser.toLowerCase()) {
            case "firefox":
                FirefoxOptions f = new FirefoxOptions();
                if (headless) f.addArguments("-headless");
                f.setAcceptInsecureCerts(true);
                f.addPreference("security.enterprise_roots.enabled", true); // доверять системным корням
                f.addPreference("security.tls.ech.enabled", false);         // отключить ECH
                f.addPreference("network.http.http3.enable", false);        // отключить HTTP/3 (QUIC)
                f.addPreference("network.dns.disableIPv6", true);
                driver = new FirefoxDriver(f);
                break;
            default:
                ChromeOptions c = new ChromeOptions();
                if (headless) {
                    c.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                }
                c.addArguments("--disable-quic");
                driver = new ChromeDriver(c);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        //Точка входа для всех тестов одна, вынесем ее в before.
        mainPage = new MainPage(driver);
        try {
            mainPage.pageOpen();
        } catch (org.openqa.selenium.WebDriverException e) {
            // короткий фолбэк на редкий сетевой сброс
            driver.navigate().to("https://qa-scooter.praktikum-services.ru");
        }
        mainPage.acceptCookies();
    }

    @After
    public void teardown() {
        driver.quit();
    }

}
