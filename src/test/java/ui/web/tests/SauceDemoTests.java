package ui.web.tests;

import com.aventstack.extentreports.*;
import commons.LogUtil;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import ui.web.base.TestBase;
import ui.web.flows.*;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SauceDemoTests extends TestBase {
    private SauceDemoLandingPage sauceDemoLandingPage;
    private SauceDemoInventoryPage sauceDemoInventoryPage;
    private SauceDemoCartPage sauceDemoCartPage;
    private WebDriver driver;
    private ExtentReports extentReports;

    public SauceDemoTests(){
    }

    @BeforeAll
    public void setup(){
        setTestData("src/test/resources/ui/sauce-demo.csv");
        setProperties("src/test/resources/ui/config.properties");
        driver = getDriver();
        sauceDemoLandingPage = new SauceDemoLandingPage(driver);
        sauceDemoInventoryPage = new SauceDemoInventoryPage(driver);
        sauceDemoCartPage = new SauceDemoCartPage(driver);
        extentReports = getReports();
    }

    @BeforeEach
    public void setupForEach(TestInfo testInfo){
        LogUtil.logInfo(this.getClass(), String.format("Starting test '%s'", testInfo.getDisplayName()));
        setScenarioData(testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Verify If a user can login to sauce demo.")
    public void positiveLoginTest(TestInfo testInfo){
        ExtentTest test = extentReports.createTest(testInfo.getDisplayName());
        for(int i = 0; i < getTestDataNumRows(); i++){
            String username = get(testInfo.getDisplayName(), "username", i);
            String password = get(testInfo.getDisplayName(), "password", i);
            ExtentTest node = test.createNode(String.format(testInfo.getDisplayName() + ". Username: '%s', Password: '%s'", username, password));
            authTestSteps(username, password, node, "positive");
        }
    }

    @Test
    @DisplayName("Verify user with invalid credentials cannot login.")
    public void negativeLoginTest(TestInfo testInfo){
        ExtentTest test = extentReports.createTest(testInfo.getDisplayName());
        for(int i = 0; i < getTestDataNumRows(); i++){
            String username = get(testInfo.getDisplayName(), "username", i);
            String password = get(testInfo.getDisplayName(), "password", i);
            ExtentTest node = test.createNode(String.format(testInfo.getDisplayName() + ". Username: '%s', Password: '%s'", username, password));
            authTestSteps(username, password, node,"");
        }
    }

    @Test
    @DisplayName("Verify a User Can Successfully Order an Item.")
    public void orderItemE2EFlow(TestInfo testInfo){
        for(int i = 0; i < getTestDataNumRows(); i++){
            String scenarioTitle = testInfo.getDisplayName();
            ExtentTest test = extentReports.createTest(scenarioTitle);
            ExtentTest iteration = test.createNode("Item to order: " + get(scenarioTitle, "itemName", i));
            String shippingInfo = get(scenarioTitle,"orderShippingInfo", i);
            double totalItemPrice = Double.parseDouble(get(scenarioTitle,"itemTotalPrice", i));

            Map<String, String> formValues = new HashMap<>();
            formValues.put("firstName",get(scenarioTitle,"firstName", i));
            formValues.put("lastName",get(scenarioTitle,"lastName", i));
            formValues.put("postalCode",get(scenarioTitle,"postalCode", i));

            sauceDemoLandingPage.launchApp(get("appUrl"), iteration.createNode("Launch application"));
            sauceDemoLandingPage.login("standard_user","secret_sauce", iteration.createNode("Login to the application"));
            sauceDemoInventoryPage.addItemToCart("Sauce Labs Backpack", iteration.createNode("Add item to cart"));
            sauceDemoCartPage.checkout(formValues, iteration.createNode("Checkout"));
            sauceDemoCartPage.verifyOrderOverview(totalItemPrice, shippingInfo, iteration.createNode("Verify order overview"));
            sauceDemoCartPage.completeOrder(iteration.createNode("Complete order"));
        }
    }

    private void authTestSteps(String username, String password, ExtentTest node, String type){
        sauceDemoLandingPage.launchApp(get("appUrl"), node.createNode("launch app"));
        sauceDemoLandingPage.login(username, password, node.createNode("login"));
        if(type.equalsIgnoreCase("positive")){
            sauceDemoLandingPage.verifySuccessfulLogin(node.createNode("Verify successful login"));
            sauceDemoLandingPage.logout(node.createNode("logout"));
        }else{
            sauceDemoLandingPage.verifyUnSuccessfulLogin(node.createNode("Verify unsuccessful login"));
        }
    }

    @AfterAll
    public void tearDown(){
        extentReports.flush();
        if(driver != null){
            driver.quit();
        }
    }

}
