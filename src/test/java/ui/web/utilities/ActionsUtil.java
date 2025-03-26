package ui.web.utilities;

import commons.LogUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class ActionsUtil {

    private final static int DEFAULT_TIMEOUT = 5;

    public void waitForElementToBeClickable(WebDriver driver,WebElement element, int timeout){
        try{
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(timeout))
                    .pollingEvery(Duration.ofMillis(1000));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "" + e);
            throw e;
        }
    }

    public void waitForElementToBeDisplayed(WebDriver driver, WebElement element, int timeout){
        boolean isDisplayed;
        try{
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(timeout))
                    .pollingEvery(Duration.ofMillis(1000));
            wait.until(ExpectedConditions.visibilityOf(element));
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "" + e);
            throw e;
        }
    }

    public void clickObject(WebDriver driver, WebElement element, int timeout){
        try{
            waitForElementToBeClickable(driver, element, timeout);
            element.click();
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "Error interacting with " + element + ". " + e);
            throw e;
        }
    }

    public void clickObject(WebDriver driver, WebElement element){
        clickObject(driver, element, DEFAULT_TIMEOUT);
    }

    public void captureData(WebDriver driver, WebElement element,String data, int timeout){
        try{
            waitForElementToBeClickable(driver, element, timeout);
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

    public boolean checkIsDisplayed(WebDriver driver, WebElement element, int timeout){
        boolean isDisplayed;
        try{
            waitForElementToBeDisplayed(driver, element,timeout);
            isDisplayed = element.isDisplayed();
            return isDisplayed;
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"Error while checking if " + element + " is displayed. " + e);
            return false;
        }
    }

    public <T> void selectItem(WebDriver driver, WebElement element,String selectBy, T selection, int timeout){
        Select select = new Select(element);
        try{
            waitForElementToBeClickable(driver, element,timeout);
            switch (selectBy.toLowerCase()){
                case "visibletext":
                    select.selectByVisibleText((String) selection);
                    break;
                case "containstext":
                    select.selectByContainsVisibleText((String) selection);
                    break;
                case "value":
                    select.selectByValue((String) selection);
                    break;
                case "index":
                    select.selectByIndex((int) selection);
                    break;
                default:
                    throw new RuntimeException("Unsupported selection method '" + selectBy + "'.");
            }
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"" + e);
            throw e;
        }
    }

    public <T> void selectItem(WebDriver driver, WebElement element,String selectBy, T selection){
        selectItem(driver, element, selectBy, selection , DEFAULT_TIMEOUT);
    }

    public void hoverOnObject(WebDriver driver, WebElement element, int timeout){
        try{
            Actions actions = new Actions(driver);
            waitForElementToBeDisplayed(driver, element, timeout);
            actions.moveToElement(element)
                    .perform();
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "" + e);
            throw e;
        }
    }

    public void hoverOnObject(WebDriver driver, WebElement element){
        hoverOnObject(driver, element, DEFAULT_TIMEOUT);
    }

    public void scrollToElement(WebDriver driver, WebElement element, int timeout){
        try{
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView(true)", element);
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "" + e);
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

    public String extractText(WebDriver driver, WebElement element, int timeout){
        try {
            waitForElementToBeDisplayed(driver, element, timeout);
            return element.getText();
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"" + e);
            return "";
        }
    }

    public String extractText(WebDriver driver, WebElement element){
        return extractText(driver, element, DEFAULT_TIMEOUT);
    }

    public String extractAttrValue(WebDriver driver, WebElement element,String attribute, int timeout){
        try{
            waitForElementToBeDisplayed(driver, element, timeout);
            return element.getDomAttribute(attribute);
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"Failed to extract attribute value. " + e.getMessage());
            throw e;
        }
    }

    public String extractAttrValue(WebDriver driver, WebElement element,String attribute){
        return extractAttrValue(driver, element, attribute, DEFAULT_TIMEOUT);
    }

}
