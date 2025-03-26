package ui.web.repositories;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.pagefactory.*;

import java.util.List;

public class SauceDemoCartPageRepo {

    public SauceDemoCartPageRepo(WebDriver driver){
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    @FindBy(id = "checkout")
    public WebElement btnCheckout;

    @FindBy(xpath = "//input[@type='text']")
    public List<WebElement> formFields;

    @FindBy(id = "first-name")
    public WebElement txtFirstFName;

    @FindBy(id = "last-name")
    public WebElement txtLastName;

    @FindBy(id = "postal-code")
    public WebElement txtPostalCode;

    @FindBy(id = "continue")
    public WebElement btnContinue;

    @FindBy(id = "finish")
    public WebElement btnFinish;

    @FindBy(id = "checkout_complete_container")
    public WebElement checkoutCompleteContainer;

    @FindBy(className = "summary_total_label")
    public WebElement priceTotalContainer;

    @FindBy(css = "div[data-test='shipping-info-value']")
    public WebElement shippingInfoContainer;

}
