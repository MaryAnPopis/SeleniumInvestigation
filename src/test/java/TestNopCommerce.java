import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import ac.cr.cenfotec.calidad.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestNopCommerce {
    private WebDriver chrome;
    private WebDriver driver;
    private WebDriverWait loadWait;
    private WebDriverWait alertWait;
    private ExpectedCondition<Boolean> domIsReady;
    private static final String WEB_PAGE_URL = "https://demo.nopcommerce.com";

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setupTest() {
        driver = new ChromeDriver();

        // Open page in different browsers
        driver.get(WEB_PAGE_URL);
    }

    @Before
    public void setupWaits() {
        loadWait = new WebDriverWait(driver, 10);
        alertWait = new WebDriverWait(driver, 1);
        domIsReady = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testWebPageTitle() {
        // Test data
        int timeout = 5;
        String expectedTitle = "nopCommerce demo store";

        // Assertion
        assertEquals(driver.getTitle(), expectedTitle);

        // Implicit timeout
        driver.manage().timeouts().implicitlyWait(timeout, SECONDS);

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
        assertEquals(driver.getCurrentUrl(), WEB_PAGE_URL + "/");

        for (Map.Entry<String, String> category : nameOfCategories.entrySet()) {
            System.out.println(category);

            // Click on Menu tab Firefix
            driver.findElement(By.linkText(category.getKey())).click();
            driver.manage().timeouts().implicitlyWait(timeout, SECONDS);
            // Check the current url after click
            assertEquals(driver.getCurrentUrl(), category.getValue());
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

        driver.findElement(wishlistSelector).click();

        // Check if the list is empty
        assertEquals(driver.findElement(noDataSelector).getText(), wishListEmptyMessage);

        // Search book in the search bar
        driver.findElement(searchBarSelector).sendKeys(bookToSearch);
        driver.findElement(searchButtonSelector).click();

        // Click in the book and go to the page details
        driver.findElement(By.linkText(productTitle)).click();

        // Add book in to the wishlist
        driver.findElement(wishListButtonSelector).click();

        // Click back to the wishlist in the green top banner
        driver.findElement(whishLitTopBannerSelector).click();

        // Check if the Fahrenheit 451 is in the wish list
        assertEquals(driver.findElement(skuBookSelector).getText(), skuFahrenheit);

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
            driver.findElement(By.linkText(category.getKey())).click();
            // Wait for the page to load
            loadWait.until(domIsReady);
            // Check the current url after click
            assertEquals(driver.getCurrentUrl(), category.getValue());
        }

        // Raise an alert with the requested message: "The test case has been executed successfully
        ((JavascriptExecutor) driver).executeScript("alert('The test case has been executed successfully')");

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

        driver.findElement(wishlistSelector).click();

        // Check if the list is empty
        assertEquals(driver.findElement(noDataSelector).getText(), wishListEmptyMessage);

        // Search book in the search bar
        driver.findElement(searchBarSelector).sendKeys(bookToSearch);
        driver.findElement(searchButtonSelector).click();

        // Click in the book and go to the page details
        driver.findElement(By.linkText(productTitle)).click();

        // Add book in to the wishlist
        driver.findElement(wishListButtonSelector).click();

        // Click back to the wishlist in the green top banner
        driver.findElement(whishLitTopBannerSelector).click();

        // Check if the Fahrenheit 451 is in the wish list
        assertEquals(driver.findElement(skuBookSelector).getText(), skuFahrenheit);

        // Select the book to be added to the cart
        driver.findElement(addToCartCheckBoxName).click();

        // Add the selected book to the cart
        driver.findElement(addToCartSelector).click();

        // Assert if the book added to the cart is the same as the searched book
        assertEquals(driver.findElement(skuBookSelector).getText(), skuFahrenheit);

        // Navigate to continue shopping
        driver.findElement(continueShoppingButtonName).click();

        // Raise an alert with the requested message: "The test case # 2 has been executed successfully"
        ((JavascriptExecutor) driver).executeScript("alert('The test case # 2 has been executed successfully')");

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
        driver.findElement(searchBarSelector).sendKeys(bookToSearch);
        driver.findElement(searchButtonSelector).click();

        // Click in the book and go to the page details
        driver.findElement(By.linkText(productTitle)).click();

        // Add book in to the wishlist
        driver.findElement(wishListButtonSelector).click();

        // Click back to the wishlist in the green top banner
        driver.findElement(whishLitTopBannerSelector).click();

        // Select the book to be added to the cart
        driver.findElement(addToCartCheckBoxName).click();

        // Add the selected book to the cart
        driver.findElement(addToCartSelector).click();

        // Create a select element with the given id
        Select countrySelectElement = new Select(driver.findElement(By.id(countrySelectID)));

        // Select the desired country
        countrySelectElement.selectByValue(desiredCountryValue);

        // Input the given postal code in to the input element.
        driver.findElement(By.id(postalCodeInputID)).sendKeys(postalCodeToBeAdded);

        // Click on the estimate shipping button
        driver.findElement(By.id(estimateShippingButtonID)).click();

        // Check the terms of service checkbox
        driver.findElement(By.id(termsOfServiceCheckboxID)).click();

        // Click on the checkout button
        driver.findElement(By.id(checkoutButtonID)).click();

        // Assert if the checkout requires you to sign in.
        assertEquals(expectedTitle, driver.getTitle());

        // Navigate to the shopping cart
        driver.navigate().to(shoppingCartURL);

        // Create a WebElement object for the quantity input
        WebElement quantityInputElement = driver.findElement(By.className(quantityInputClassName));

        // Clear the text of the quantity input
        quantityInputElement.clear();

        // Asign the desired value to the quantity input
        quantityInputElement.sendKeys(desiredQuantity);

        // Update the contents of the shopping cart
        driver.findElement(By.name(updateCartButtonName)).click();

        // Assert if the shopping cart is empty
        assertEquals(emptyShoppingCartMessage, driver.findElement(By.className(noDataClassName)).getText());

        // Raise an alert with the success message
        ((JavascriptExecutor) driver).executeScript("alert('"+ successMessage +"')");

        // Wait for the alert to be closed
        while (isAlertPresent());

        // Close the browser
        driver.quit();

        // Print the success message
        System.out.println(successMessage);
    }

    @Test
    public void testList() {
        String category = "Computers";
        String firstLowHigh = "Sound Forge Pro 11 (recurring)";
        String firstHighLow = "Adobe Photoshop CS4";
        By subCategory = By.cssSelector(".item-grid > div:nth-child(3)");
        By listView = By.cssSelector(".product-viewmode > a:nth-child(3)");
        By firstProducSort = By.cssSelector(".details h2 a");
        String highLowURL = WEB_PAGE_URL + "/software?viewmode=list&orderby=11";
        By sortDropDown = By.name("products-orderby");

        driver.findElement(By.linkText(category)).click();
        driver.findElement(subCategory).click();
        driver.findElement(listView).click();

        Select select = new Select(driver.findElement(sortDropDown));
        select.selectByIndex(3);

        // Check that the first result in sort low to high
        assertEquals(driver.findElement(firstProducSort).getText(), firstLowHigh);

        // Implicit timeout
        driver.manage().timeouts().implicitlyWait(10, SECONDS);
        Select select2 = new Select(driver.findElement(sortDropDown));
        select2.selectByIndex(4);
        assertEquals(driver.getCurrentUrl(), highLowURL);

        // Check that the first result in sort high to low
        assertEquals(driver.findElement(firstProducSort).getText(), firstHighLow);

        // Raise an alert with the requested message: "The test case has been executed successfully
        ((JavascriptExecutor) driver).executeScript("alert('The test case has been executed successfully')");

        // Wait until the alert is dismissed
        while (isAlertPresent());
    }

    @Test
    public void testExcel ()throws IOException {
        String wishListEmptyMessage = "The wishlist is empty!";
        By wishlistSelector = By.cssSelector(".ico-wishlist");
        By noDataSelector = By.cssSelector(".no-data");
        By inputSearch = By.id("small-searchterms");
        By searchButtonSelector = By.xpath("//input[@class='button-1 search-box-button']");
        By wishListButtonSelector = By.cssSelector(".add-to-wishlist-button");
        By noResult = By.cssSelector(".no-result");
        List<WebElement> quatityInputs = driver.findElements(By.cssSelector(".qty-input"));

        driver.findElement(wishlistSelector).click();

        // Check if the list is empty
        assertEquals(driver.findElement(noDataSelector).getText(), wishListEmptyMessage);


        ArrayList<Product> products = saveExcelOnList();
        int counter = 0;
        for (Product p : products) {
            driver.findElement(inputSearch).sendKeys(p.getName());
            driver.findElement(searchButtonSelector).click();

            if(driver.findElements( noResult ).size()  > 0){
                driver.findElement(wishlistSelector).click();
            } else {
                driver.findElement(By.linkText(p.getName())).click();
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                driver.findElement(wishListButtonSelector).click();
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
               // quatityInputs.get(counter).sendKeys(String.valueOf(p.getQuantity()));
                driver.findElement(inputSearch).sendKeys("");
            }

            counter++;
         }



        // Raise an alert with the requested message: "The test case has been executed successfully
        ((JavascriptExecutor) driver).executeScript("alert('The test case has been executed successfully')");

        // Wait until the alert is dismissed
        while (isAlertPresent());
    }

    private ArrayList saveExcelOnList() throws IOException{
        String workingDirectory = System.getProperty("user.dir");
        String fileName = "Parametros.xlsx";
        // Read excel
        File excelFile = new File(workingDirectory + "\\" +fileName);
        FileInputStream fis = new FileInputStream(excelFile);

        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIt = sheet.iterator();
        rowIt.next();

        ArrayList<Product> products = new ArrayList<Product>();
        while(rowIt.hasNext()) {

            Row row = rowIt.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            Product newProduct = new Product();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();


                if(cell.getCellType() == CellType.STRING) {
                    String myCellValue = cell.getStringCellValue();

                    if(myCellValue.startsWith("$")){
                        newProduct.setPrice(Double.parseDouble(myCellValue.substring(1)));
                    }else {
                        newProduct.setName(myCellValue);
                    }
                }

                if(cell.getCellType() == CellType.NUMERIC) {
                    newProduct.setQuantity( (int)cell.getNumericCellValue() );
                }
            }
            products.add(newProduct);
        }

        workbook.close();
        fis.close();

        return products;
    }

}
