package ru.yandex.praktikum.scooter.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.time.Duration;

public class OrderPage {
    private WebDriver driver;

    private By headOrder = By.xpath("//div[text()='Для кого самокат']");

    private By nameField = By.xpath("//input[@placeholder='* Имя']");
    private By surnameField = By.xpath("//input[@placeholder='* Фамилия']");
    private By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private By metroStationField = By.xpath("//input[@placeholder='* Станция метро']");
    private final String metroStationMenu = "//div[text()='%s']";
    private By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private By nextButton = By.xpath("//button[text()='Далее']");

    private By dateField = By.xpath("//div[@class='react-datepicker-wrapper']/div/input");
    private By timeField = By.className("Dropdown-placeholder");
    private final String timeDropDown = "//div[text()='%s']";
    private By blackCheckBox = By.id("black");
    private By greyCheckBox = By.id("grey");
    private By commentField = By.xpath("//div[@class='Input_InputContainer__3NykH']/input[contains(@class,'Input_Responsible')]");
    private By orderDown = By.xpath("//button[contains(@class, 'Button_Middle__1CSJM') and text()='Заказать']");

    private By yesButton = By.xpath("//button[text()='Да']");
    private By orderPass = By.className("Order_ModalHeader__3FDaJ");
    private By textAboutOrderWithNumber = By.xpath("//div[@class='Order_Text__2broi']");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void setSurname(String surname) {
        driver.findElement(surnameField).sendKeys(surname);
    }

    public void setAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    public void setMetroStation(String metroStation) {
        driver.findElement(metroStationField).click();
        WebElement stationMetroElement = driver.findElement(By.xpath(String.format(metroStationMenu, metroStation)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", stationMetroElement);
        stationMetroElement.click();
    }

    public void setPhone(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
    }

    public void clickNextButton() {
        WebElement button = driver.findElement(nextButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
    }

    public void setData(String data) {
        driver.findElement(dateField).sendKeys(data, Keys.ENTER);
    }

    public void setTime(String time) {
        driver.findElement(timeField).click();
        driver.findElement(By.xpath(String.format(timeDropDown, time))).click();
    }

    public void setColor(String color) {
        if (color.equals("чёрный жемчуг")) {
            driver.findElement(blackCheckBox).click();
        } else if (color.equals("серая безысходность")) {
            driver.findElement(greyCheckBox).click();
        }
    }

    public void setComment(String comment) {
        driver.findElement(commentField).sendKeys(comment);
    }

    public void clickOrderDown() {
        WebElement button = driver.findElement(orderDown);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
    }

    public void clickYesButton() {
        WebElement button = driver.findElement(yesButton);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
    }

    public boolean getStatusNumber() {
        WebElement element = driver.findElement(textAboutOrderWithNumber);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
        String fullText = element.getText();
//  парсинг строки для получения номера заказа
        String orderNumber = fullText.substring(14, 20);
//  регулярное выражение для проверки формата
        String numberRegex = "^\\d{6}$";
// компилирование регулярного выражения в шаблон
        Pattern pattern = Pattern.compile(numberRegex);
// создание объекта Matcher для проверки строки
        Matcher matcher = pattern.matcher(orderNumber);
// проверка строки на соответствие регулярному выражению
        return matcher.matches();
    }

    public void fillingFormForWhom(String name, String surname, String address, String metroStation, String phone) {
        setName(name);
        setSurname(surname);
        setAddress(address);
        setMetroStation(metroStation);
        setPhone(phone);
        clickNextButton();
    }

    public void fillingFormAboutRent(String data, String time, String color, String comment) {
        setData(data);
        setTime(time);
        setColor(color);
        setComment(comment);
        clickOrderDown();
        clickYesButton();
    }

    // Проверка наличия элемента "Заказ оформлен" на всплывающем окне
    public boolean findElementOrderPass() {
        WebElement element = driver.findElement(orderPass);
        return element.isDisplayed();
    }

    public boolean checkSuccessOrderCreation() {
        return getStatusNumber() && findElementOrderPass();
    }

    public boolean checkOpenOrderPage() {
        WebElement element = driver.findElement(headOrder);
        if (element != null) {
            return true;
        } else {
            return false;
        }
    }
}
