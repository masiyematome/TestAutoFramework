package ui.web.flows;

import com.aventstack.extentreports.ExtentTest;
import commons.*;
import org.openqa.selenium.*;
import org.opentest4j.AssertionFailedError;
import ui.web.repositories.SauceDemoCartPageRepo;
import ui.web.utilities.ActionsUtil;
import java.util.Map;
import java.util.regex.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SauceDemoCartPage extends ActionsUtil {
    private final SauceDemoCartPageRepo cartPageRepo;
    private final WebDriver driver;

    public SauceDemoCartPage(WebDriver driver){
        this.driver = driver;
        cartPageRepo = new SauceDemoCartPageRepo(driver);
    }

    public void checkout(Map<String, String> formValues,ExtentTest node){
        try{
            clickObject(driver, cartPageRepo.btnCheckout);
            for(WebElement input: cartPageRepo.formFields){
                populateInformationForm(input, formValues);
            }
            node.pass("Successfully populated customer information form.", ExtentUtil.getScreenshot(driver));
            LogUtil.logInfo(this.getClass(), "Successfully populated customer information form.");
            clickObject(driver, cartPageRepo.btnContinue);
        }catch (Exception e){
            node.fail("Failed to checkout.", ExtentUtil.getScreenshot(driver));
            LogUtil.logError(this.getClass(), "Error during checkout. " + e.getMessage());
            throw e;
        }
    }

    private void populateInformationForm(WebElement input, Map<String, String> formValues){
        try{
            for(Map.Entry<String, String> kvp: formValues.entrySet()){
                String dataTest = extractAttrValue(driver, input, "data-test");
                if(dataTest.equalsIgnoreCase(kvp.getKey())){
                    captureData(driver, input, kvp.getValue());
                }
            }
        }catch (Exception e){
            LogUtil.logError(this.getClass(), "Couldn't extract data-test attribute value. " + e.getMessage());
            throw new RuntimeException("Couldn't extract data-test attribute value. ", e);
        }
    }

    public void verifyOrderOverview(double expectedPriceTotal, String expectedShippingInfo, ExtentTest node){
        try{
            double actualPriceTotal = extractDoubleFromString(extractText(driver, cartPageRepo.priceTotalContainer));
            String actualShippingInformation = extractText(driver, cartPageRepo.shippingInfoContainer);
            assertThat(actualPriceTotal).isEqualTo(expectedPriceTotal);
            assertThat(actualShippingInformation).isEqualTo(expectedShippingInfo);
            node.pass("Order information matches the expected information.", ExtentUtil.getScreenshot(driver));
            LogUtil.logInfo(this.getClass(), "Order information is matches the expected information.");
        }catch (Exception | AssertionFailedError e){
            node.fail("Incorrect order info.", ExtentUtil.getScreenshot(driver));
            LogUtil.logError(this.getClass(), "Incorrect order info. " + e.getMessage());
            throw e;
        }
    }

    private double extractDoubleFromString(String input){
        String regex = "[-+]?\\d*\\.?\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(!matcher.find()){
            throw new RuntimeException("No valid number found in the input '" + input + "'");
        }
        return Double.parseDouble(matcher.group());
    }

    public void completeOrder(ExtentTest node){
        try {
            clickObject(driver, cartPageRepo.btnFinish);
            waitForElementToBeDisplayed(driver, cartPageRepo.checkoutCompleteContainer,5);
            String checkoutCompleteMessage = extractText(driver, cartPageRepo.checkoutCompleteContainer);
            assertThat(checkoutCompleteMessage.contains("Thank you for your order!")).isTrue();
            node.pass("Successfully checked-out", ExtentUtil.getScreenshot(driver));
            LogUtil.logInfo(this.getClass(), "Successfully checked-out.");
        }catch (Exception | AssertionFailedError e){
            LogUtil.logError(this.getClass(), "Unsuccessful checkout. " + e.getMessage());
            node.fail("Unsuccessful checkout", ExtentUtil.getScreenshot(driver));
            throw e;
        }
    }

}
