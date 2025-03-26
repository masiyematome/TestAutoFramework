package ui.web.flows;

import com.aventstack.extentreports.ExtentTest;
import commons.*;
import org.openqa.selenium.*;
import ui.web.repositories.SauceDemoInventoryPageRepo;
import ui.web.utilities.ActionsUtil;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SauceDemoInventoryPage extends ActionsUtil {
    private final WebDriver driver;
    private final SauceDemoInventoryPageRepo inventoryPageRepo;

    public SauceDemoInventoryPage(WebDriver driver){
        this.driver = driver;
        inventoryPageRepo = new SauceDemoInventoryPageRepo(driver);
    }

    public void addItemToCart(String itemName, ExtentTest node){
        String itemType = "inventory";
        try{
            WebElement addButton = inventoryPageRepo.getItemElement(itemType, itemName, "add");
            String itemPrice = extractText(driver,inventoryPageRepo.getItemElement(itemType, itemName, "price"));
            String itemDescription = extractText(driver, inventoryPageRepo.getItemElement(itemType, itemName, "description"));
            clickObject(driver, addButton);
            validateItemWasAdded(itemName, itemPrice, itemDescription);
            node.pass("Item successfully added to cart", ExtentUtil.getScreenshot(driver));
            LogUtil.logInfo(this.getClass(), "Item '" + itemName + "' added to cart.");
        }catch (Exception e){
            node.fail("Item not added to cart", ExtentUtil.getScreenshot(driver));
            LogUtil.logError(this.getClass(), "Error while adding item to cart. " + e.getMessage());
            throw e;
        }
    }

    private void validateItemWasAdded(String itemName, String price, String description){
        try{
            clickObject(driver, inventoryPageRepo.linkCart);
            String priceInTheCart = extractText(driver, inventoryPageRepo.getItemElement("cart",itemName, "price"));
            String descriptionInTheCart = extractText(driver, inventoryPageRepo.getItemElement("cart",itemName, "description"));
            assertThat(priceInTheCart).isEqualTo(price);
            assertThat(descriptionInTheCart).isEqualTo(description);
        }catch (Exception e){
            throw new RuntimeException("No element matching '"+ itemName + "' found in the cart.", e);
        }
    }

}
