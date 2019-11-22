import static org.junit.jupiter.api.Assertions.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Hashtable;
import java.util.Map;

public class TestNopCommerce {
    private WebDriver chrome;
    private WebDriver firefox;
    private WebDriverWait loadWait;
    private WebDriverWait alertWait;
    private ExpectedCondition<Boolean> domIsReady;
    private static final String WEB_PAGE_URL = "https://demo.nopcommerce.com";

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
    }

    @Before
    public void setupTest() {
        chrome = new ChromeDriver();
        firefox = new FirefoxDriver();

        // Open page in different browsers
        chrome.get(WEB_PAGE_URL);
        firefox.get(WEB_PAGE_URL);
    }

    @Before
    public void setupWaits() {
        loadWait = new WebDriverWait(firefox, 10);
        alertWait = new WebDriverWait(firefox, 1);
        domIsReady = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
    }

    @After
    public void teardown() {
        if (chrome != null) {
            chrome.quit();
        }
        if (firefox != null) {
            firefox.quit();
        }
    }

    @Test
    public void testWebPageTitle() {
        // Test data
        int timeout = 5;
        String expectedTitle = "nopCommerce demo store";

        // Assertion
        assertEquals(chrome.getTitle(), expectedTitle);
        assertEquals(firefox.getTitle(), expectedTitle);

        // Implicit timeout
        chrome.manage().timeouts().implicitlyWait(timeout, SECONDS);
        firefox.manage().timeouts().implicitlyWait(timeout, SECONDS);

        System.out.println("Title is correct");
    }


    @Test
    public void testMenu() {
        int timeout = 3;

        Hashtable<String, String> nameOfCategories = new Hashtable<String, String>();
        nameOfCategories.put("Computers", WEB_PAGE_URL + "/computers");
        nameOfCategories.put("Electronics", WEB_PAGE_URL + "/electronics");
        nameOfCategories.put("Apparel", WEB_PAGE_URL + "/apparel");
        nameOfCategories.put("Digital downloads", WEB_PAGE_URL + "/digital-downloads");
        nameOfCategories.put("Books", WEB_PAGE_URL + "/books");
        nameOfCategories.put("Jewelry", WEB_PAGE_URL + "/jewelry");
        nameOfCategories.put("Gift Cards", WEB_PAGE_URL + "/gift-cards");

        // Check the home page url
        assertEquals(chrome.getCurrentUrl(), WEB_PAGE_URL + "/");
        assertEquals(firefox.getCurrentUrl(), WEB_PAGE_URL + "/");

        for (Map.Entry<String, String> category : nameOfCategories.entrySet()) {
            System.out.println(category);
            // Click on Menu tab Chrome
            chrome.findElement(By.linkText(category.getKey())).click();
            chrome.manage().timeouts().implicitlyWait(timeout, SECONDS);
            // Check the current url after click
            assertEquals(chrome.getCurrentUrl(), category.getValue());

            // Click on Menu tab Chrome
            firefox.findElement(By.linkText(category.getKey())).click();
            firefox.manage().timeouts().implicitlyWait(timeout, SECONDS);
            // Check the current url after click
            assertEquals(firefox.getCurrentUrl(), category.getValue());
        }
        System.out.println("Menu tabs are correct");
    }

    @Test
    public void addProductToWishList() {
        String wishListEmptyMessage = "The wishlist is empty!";
        String productTitle = "Fahrenheit 451 by Ray Bradbury";
        String skuFahrenheit = "FR_451_RB";
        String bookToSearch = "Fahrenheit 45";

        By wishlistSelector = By.cssSelector(".ico-wishlist");
        By noDataSelector = By.cssSelector(".no-data");
        By searchBarSelector = By.xpath("//input[@id='small-searchterms']");
        By searchButtonSelector = By.xpath("//input[@class='button-1 search-box-button']");
        By wishListButtonSelector = By.cssSelector("#add-to-wishlist-button-37");
        By skuBookSelector = By.className("sku-number");
        By whishLitTopBannerSelector = By.cssSelector("a[href='/wishlist']");

        chrome.findElement(wishlistSelector).click();
        firefox.findElement(wishlistSelector).click();

        // Check if the list is empty
        assertEquals(chrome.findElement(noDataSelector).getText(), wishListEmptyMessage);
        assertEquals(firefox.findElement(noDataSelector).getText(), wishListEmptyMessage);

        // Search book in the search bar
        chrome.findElement(searchBarSelector).sendKeys(bookToSearch);
        chrome.findElement(searchButtonSelector).click();

        firefox.findElement(searchBarSelector).sendKeys(bookToSearch);
        firefox.findElement(searchButtonSelector).click();

        // Click in the book and go to the page details
        chrome.findElement(By.linkText(productTitle)).click();
        firefox.findElement(By.linkText(productTitle)).click();

        // Add book in to the wishlist
        chrome.findElement(wishListButtonSelector).click();
        firefox.findElement(wishListButtonSelector).click();

        // Click back to the wishlist in the green top banner
        chrome.findElement(whishLitTopBannerSelector).click();
        firefox.findElement(whishLitTopBannerSelector).click();


        // Check if the Fahrenheit 451 is in the wish list
        assertEquals(chrome.findElement(skuBookSelector).getText(), skuFahrenheit);
        assertEquals(firefox.findElement(skuBookSelector).getText(), skuFahrenheit);

        System.out.println("Adding a book into the wish list is correct!");
    }

    //    Second investigation tests start here.

    //TC-1
    @Test
    public void testCategories() {

        //Hashtable with the categories
        Hashtable<String, String> nameOfCategories = new Hashtable<String, String>();
        nameOfCategories.put("Computers", WEB_PAGE_URL + "/computers");
        nameOfCategories.put("Electronics", WEB_PAGE_URL + "/electronics");
        nameOfCategories.put("Apparel", WEB_PAGE_URL + "/apparel");
        nameOfCategories.put("Digital downloads", WEB_PAGE_URL + "/digital-downloads");
        nameOfCategories.put("Books", WEB_PAGE_URL + "/books");
        nameOfCategories.put("Jewelry", WEB_PAGE_URL + "/jewelry");
        nameOfCategories.put("Gift Cards", WEB_PAGE_URL + "/gift-cards");

        for (Map.Entry<String, String> category : nameOfCategories.entrySet()) {
            System.out.println(category);
            // Click on Menu tab Chrome
            firefox.findElement(By.linkText(category.getKey())).click();
            // Wait for the page to load
            loadWait.until(domIsReady);
            // Check the current url after click
            assertEquals(firefox.getCurrentUrl(), category.getValue());
        }

        // Raise an alert with the requested message: "The test case has been executed successfully
        ((JavascriptExecutor) firefox).executeScript("alert('The test case has been executed successfully')");

        // Wait until the alert is dismissed
        while (isAlertPresent());
    }

    private boolean isAlertPresent() {
        try {
            alertWait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Test
    public void testShoppingCart() {
        String wishListEmptyMessage = "The wishlist is empty!";
        String productTitle = "Fahrenheit 451 by Ray Bradbury";
        String skuFahrenheit = "FR_451_RB";
        String bookToSearch = "Fahrenheit 451";

        By wishlistSelector = By.cssSelector(".ico-wishlist");
        By noDataSelector = By.cssSelector(".no-data");
        By searchBarSelector = By.id("small-searchterms");
        By searchButtonSelector = By.xpath("//input[@class='button-1 search-box-button']");
        By wishListButtonSelector = By.cssSelector("#add-to-wishlist-button-37");
        By skuBookSelector = By.className("sku-number");
        By whishLitTopBannerSelector = By.cssSelector("a[href='/wishlist']");
        By addToCartCheckBoxName = By.name("addtocart");
        By continueShoppingButtonName = By.name("continueshopping");
        By addToCartSelector = By.className("wishlist-add-to-cart-button");

        firefox.findElement(wishlistSelector).click();

        // Check if the list is empty
        assertEquals(firefox.findElement(noDataSelector).getText(), wishListEmptyMessage);

        // Search book in the search bar
        firefox.findElement(searchBarSelector).sendKeys(bookToSearch);
        firefox.findElement(searchButtonSelector).click();

        // Click in the book and go to the page details
        firefox.findElement(By.linkText(productTitle)).click();

        // Add book in to the wishlist
        firefox.findElement(wishListButtonSelector).click();

        // Click back to the wishlist in the green top banner
        firefox.findElement(whishLitTopBannerSelector).click();

        // Check if the Fahrenheit 451 is in the wish list
        assertEquals(firefox.findElement(skuBookSelector).getText(), skuFahrenheit);

        // Select the book to be added to the cart
        firefox.findElement(addToCartCheckBoxName).click();

        // Add the selected book to the cart
        firefox.findElement(addToCartSelector).click();

        // Assert if the book added to the cart is the same as the searched book
        assertEquals(firefox.findElement(skuBookSelector).getText(), skuFahrenheit);

        // Navigate to continue shopping
        firefox.findElement(continueShoppingButtonName).click();

        // Raise an alert with the requested message: "The test case # 2 has been executed successfully"
        ((JavascriptExecutor) firefox).executeScript("alert('The test case # 2 has been executed successfully')");

        // Wait until the alert is dismissed
        while (isAlertPresent()) ;

    }

    @Test
    public void testCheckout(){
        // Set the values needed for the test
        String bookToSearch = "Fahrenheit 451";
        String productTitle = "Fahrenheit 451 by Ray Bradbury";

        By searchBarSelector = By.id("small-searchterms");
        By searchButtonSelector = By.xpath("//input[@class='button-1 search-box-button']");
        By wishListButtonSelector = By.cssSelector("#add-to-wishlist-button-37");
        By whishLitTopBannerSelector = By.cssSelector("a[href='/wishlist']");
        By addToCartCheckBoxName = By.name("addtocart");
        By addToCartSelector = By.className("wishlist-add-to-cart-button");

        String shoppingCartURL = "https://demo.nopcommerce.com/cart";
        String countrySelectID = "CountryId";
        String desiredCountryValue = "174";
        String postalCodeInputID = "ZipPostalCode";
        String postalCodeToBeAdded = "1000-1";
        String estimateShippingButtonID = "estimate-shipping-button";
        String termsOfServiceCheckboxID = "termsofservice";
        String checkoutButtonID = "checkout";
        String expectedTitle = "nopCommerce demo store. Login";
        String quantityInputClassName = "qty-input";
        String desiredQuantity = "0";
        String updateCartButtonName = "updatecart";
        String emptyShoppingCartMessage = "Your Shopping Cart is empty!";
        String noDataClassName = "no-data";
        String successMessage = "The test case # 3 has been executed successfully.";

        // Search book in the search bar
        firefox.findElement(searchBarSelector).sendKeys(bookToSearch);
        firefox.findElement(searchButtonSelector).click();

        // Click in the book and go to the page details
        firefox.findElement(By.linkText(productTitle)).click();

        // Add book in to the wishlist
        firefox.findElement(wishListButtonSelector).click();

        // Click back to the wishlist in the green top banner
        firefox.findElement(whishLitTopBannerSelector).click();

        // Select the book to be added to the cart
        firefox.findElement(addToCartCheckBoxName).click();

        // Add the selected book to the cart
        firefox.findElement(addToCartSelector).click();

        // Create a select element with the given id
        Select countrySelectElement = new Select(firefox.findElement(By.id(countrySelectID)));

        // Select the desired country
        countrySelectElement.selectByValue(desiredCountryValue);

        // Input the given postal code in to the input element.
        firefox.findElement(By.id(postalCodeInputID)).sendKeys(postalCodeToBeAdded);

        // Click on the estimate shipping button
        firefox.findElement(By.id(estimateShippingButtonID)).click();

        // Check the terms of service checkbox
        firefox.findElement(By.id(termsOfServiceCheckboxID)).click();

        // Click on the checkout button
        firefox.findElement(By.id(checkoutButtonID)).click();

        // Assert if the checkout requires you to sign in.
        assertEquals(expectedTitle, firefox.getTitle());

        // Navigate to the shopping cart
        firefox.navigate().to(shoppingCartURL);

        // Create a WebElement object for the quantity input
        WebElement quantityInputElement = firefox.findElement(By.className(quantityInputClassName));

        // Clear the text of the quantity input
        quantityInputElement.clear();

        // Asign the desired value to the quantity input
        quantityInputElement.sendKeys(desiredQuantity);

        // Update the contents of the shopping cart
        firefox.findElement(By.name(updateCartButtonName)).click();

        // Assert if the shopping cart is empty
        assertEquals(emptyShoppingCartMessage, firefox.findElement(By.className(noDataClassName)).getText());

        // Raise an alert with the success message
        ((JavascriptExecutor) firefox).executeScript("alert('"+ successMessage +"')");

        // Close the browser
        firefox.quit();

        // Print the success message
        System.out.println(successMessage);
    }

}
