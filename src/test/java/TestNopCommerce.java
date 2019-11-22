import static org.junit.jupiter.api.Assertions.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
        String bookToSearch = "Fahrenheit 45";

        By wishlistSelector = By.cssSelector(".ico-wishlist");
        By noDataSelector = By.cssSelector(".no-data");
        By searchBarSelector = By.xpath("//input[@id='small-searchterms']");
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

}
