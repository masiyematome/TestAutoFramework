package ui.web.flows;

import com.aventstack.extentreports.ExtentTest;
import commons.*;
import org.openqa.selenium.WebDriver;
import ui.web.repositories.*;
import ui.web.utilities.ActionsUtil;

public class SauceDemoLandingPage extends ActionsUtil {
    private final WebDriver driver;
    private final SauceDemoLandingPageRepo landingPageRepo;
    private final SauceDemoInventoryPageRepo inventoryPageRepo;

    public SauceDemoLandingPage(WebDriver driver){
        this.driver = driver;
        landingPageRepo = new SauceDemoLandingPageRepo(driver);
        inventoryPageRepo = new SauceDemoInventoryPageRepo(driver);
    }

    public void launchApp(String url, ExtentTest node){
        try{
            driver.get(url);
            LogUtil.logInfo(this.getClass(),"Launched '" + url + "'");
            node.pass("App launched successfully.", ExtentUtil.getScreenshot(driver));
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"Couldn't launch '" + url + "'. " + e);
            node.fail("Failed to launch app.", ExtentUtil.getScreenshot(driver));
            throw e;
        }
    }

    public void login(String username, String password, ExtentTest node){
        try{
            captureData(driver, landingPageRepo.txtUsername, username);
            captureData(driver, landingPageRepo.txtPassword, password);
            clickObject(driver, landingPageRepo.btnLogin);
            node.pass("Logged in successfully",ExtentUtil.getScreenshot(driver));
            LogUtil.logInfo(this.getClass(), "Logged in with username: " + username);
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "Login failed. Error: " + e.getMessage());
            node.fail(ExtentUtil.getScreenshot(driver));
            throw e;
        }
    }

    public void verifySuccessfulLogin(ExtentTest node){
        boolean isDisplayed = checkIsDisplayed(inventoryPageRepo.divProducts);
        if(!isDisplayed){
            LogUtil.logError(this.getClass(), "Login failed. Expected app element not present.");
            node.fail("Failed to login.", ExtentUtil.getScreenshot(driver));
            throw new RuntimeException("Login failed. Expected app element not present.");
        }
        node.pass("Logged in successfully", ExtentUtil.getScreenshot(driver));
    }

    public void verifyUnSuccessfulLogin(ExtentTest node){
        try{
            waitForElementToBeDisplayed(driver, landingPageRepo.loginErrorMessage, 2);
            node.pass("Failed to login.", ExtentUtil.getScreenshot(driver));
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "No login error message displayed.");
            node.fail("No login error message displayed.");
            throw new RuntimeException("No login error message displayed.");
        }
    }

    public void logout(ExtentTest node){
        try{
            clickObject(driver, inventoryPageRepo.btnOpenMenu);
            clickObject(driver, inventoryPageRepo.btnLogout);
            node.pass("Logged out of application successfully.", ExtentUtil.getScreenshot(driver));
        }catch (Exception e){
            LogUtil.logError(this.getClass(),"Failed to logout of application.");
            node.fail("Failed to logout of application.", ExtentUtil.getScreenshot(driver));
            throw e;
        }
    }

}
