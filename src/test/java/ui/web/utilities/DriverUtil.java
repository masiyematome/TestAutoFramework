package ui.web.utilities;

import commons.LogUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverUtil {

    private static WebDriver driver;

    public static void initializeDriver(String browserName, boolean headless){
        switch (browserName.toLowerCase()){
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxoptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxoptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                driver = new EdgeDriver(edgeOptions);
                break;
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if(headless) chromeOptions.addArguments("--headless");
                driver = new ChromeDriver(chromeOptions);
                break;
            default:
                LogUtil.logError(DriverUtil.class, "'" + browserName + "'" + " is not a supported browser.");
                throw new IllegalArgumentException("'" + browserName + "'" + " is not a supported browser.");
        }
        driver.manage().window().maximize();
    }

    public static WebDriver getDriver(){
        if(driver == null){
            LogUtil.logError(DriverUtil.class, "WebDriver cannot be null. Call initializeDriver first");
            throw new RuntimeException("WebDriver cannot be null. Call initializeDriver first");
        }
        return driver;
    }

}

