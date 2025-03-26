package ui.web.repositories;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.pagefactory.*;

public class SauceDemoLandingPageRepo {

    public SauceDemoLandingPageRepo(WebDriver driver){
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    @FindBy(id = "user-name")
    public WebElement txtUsername;

    @FindBy(id = "password")
    public WebElement txtPassword;

    @FindBy(id = "login-button")
    public WebElement btnLogin;

    @FindBy(css = "div[class*='error']")
    public WebElement loginErrorMessage;

}
