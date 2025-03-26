package ui.web.repositories;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.util.List;

public class SauceDemoInventoryPageRepo {

    public SauceDemoInventoryPageRepo(WebDriver driver){
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    @FindBy(id = "inventory_container")
    public WebElement divProducts;

    @FindBy(id = "logout_sidebar_link")
    public WebElement btnLogout;

    @FindBy(id = "react-burger-menu-btn")
    public WebElement btnOpenMenu;

    @FindBy(xpath = "//div[@class='inventory_item']")
    public List<WebElement> products;

    @FindBy(xpath = "//div[@class='cart_item']")
    public List<WebElement> cartItems;

    @FindBy(css = ".shopping_cart_link")
    public WebElement linkCart;

    public WebElement getItemElement(String itemType, String itemName, String targetElement){
        WebElement matchedItem = null;
        List<WebElement> items = itemType.equalsIgnoreCase("inventory")? products:cartItems;
        for(WebElement element: items){
            String itemHeading = element.getText().toLowerCase();
            if(itemHeading.contains(itemName.toLowerCase())){
                matchedItem = element;
                break;
            }
        }
        return getTargetElement(matchedItem, itemName, targetElement);
    }

    private WebElement getTargetElement(WebElement parentElement,String itemName,  String targetElement){
        if(parentElement == null){
            throw new RuntimeException("No element matching '" + itemName + "' found");
        }
        return switch (targetElement.toLowerCase()){
            case "price" -> parentElement.findElement(By.cssSelector(".inventory_item_price"));
            case "add" -> parentElement.findElement(By.xpath(".//button[contains(text(),'Add')]"));
            case "description" -> parentElement.findElement(By.cssSelector(".inventory_item_desc"));
            default -> throw new InvalidArgumentException("'" + targetElement + "' is not a valid element.");
        };
    }

}
