package ui.web.utilities;

import commons.LogUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class ActionsUtil {

    private final static int DEFAULT_TIMEOUT = 5;

    public void launchApp(WebDriver driver, String url){
        try{
            driver.get(url);
            LogUtil.logInfo(this.getClass(),"Launched '" + url + "'");
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"Couldn't launch '" + url + "'. " + e);
            throw e;
        }
    }

    public void waitForElementToBeClickable(WebDriver driver,WebElement element, int duration){
        try{
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(duration))
                    .pollingEvery(Duration.ofMillis(1000));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "" + e);
            throw e;
        }
    }

    public void waitForElementToBeDisplayed(WebDriver driver, WebElement element, int duration){
        try{
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(duration))
                    .pollingEvery(Duration.ofMillis(1000));
            wait.until(ExpectedConditions.visibilityOf(element));
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "" + e);
            throw e;
        }
    }

    public void clickObject(WebDriver driver, WebElement element, int duration){
        try{
            waitForElementToBeClickable(driver, element, duration);
            element.click();
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "Error interacting with " + element + ". " + e);
            throw e;
        }
    }

    public void clickObject(WebDriver driver, WebElement element){
        clickObject(driver, element, DEFAULT_TIMEOUT);
    }

    public void captureData(WebDriver driver, WebElement element,String data, int duration){
        try{
            waitForElementToBeClickable(driver, element, duration);
            element.clear();
            element.sendKeys(data);
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "" + e);
            throw e;
        }
    }

    public void captureData(WebDriver driver, WebElement element,String data){
        captureData(driver, element, data, DEFAULT_TIMEOUT);
    }

    public boolean checkIsDisplayed(WebDriver driver, WebElement element, int duration){
        boolean isDisplayed;
        try{
            waitForElementToBeDisplayed(driver, element,duration);
            isDisplayed = element.isDisplayed();
            return isDisplayed;
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"Error while checking if " + element + " is displayed. " + e);
            return false;
        }
    }

    public boolean checkIsDisplayed(WebElement element){
        boolean isDisplayed;
        try{
            isDisplayed = element.isDisplayed();
            return isDisplayed;
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"Error while checking if " + element + " is displayed. " + e);
            return false;
        }
    }

}
