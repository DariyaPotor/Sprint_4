package ru.yandex.praktikum.scooter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import ru.yandex.praktikum.scooter.pages.MainPage;
import ru.yandex.praktikum.scooter.pages.OrderPage;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class Order {
    private WebDriver driver;

    private final String name;
    private final String surname;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String data;
    private final String time;
    private final String color;
    private final String comment;

    public Order(String name, String surname, String address, String metroStation, String phone, String data, String time, String color, String comment) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metroStation = metroStation;
        this.data = data;
        this.phone = phone;
        this.time = time;
        this.color = color;
        this.comment = comment;
    }

    //Тестовые данные
    @Parameterized.Parameters
    public static Object[][] getSumData() {
        return new Object[][]{
                {"Петя", "Новый", "Москва, Театральная 10", "Театральная", "+79111234567", "15.10.2024", "двое суток", "чёрный жемчуг", "Хочу желтые колеса"},
                {"Миша", "Старый", "Москва, Новоданиловская наб. 10", "Тульская", "+79117654321", "07.12.2024", "сутки", "серая безысходность", ""},
        };
    }

    @Before
    public void setup() {
        driver = WebDriverFactory.getWebDriver(System.getProperty("browser", "firefox"));
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    @Test
    public void newOrderPass() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickButtonOrderUp();
        mainPage.clickCookieButton();
        OrderPage orderPage = new OrderPage(driver);
        orderPage.fillingFormForWhom(name, surname, address, metroStation, phone);
        orderPage.fillingFormAboutRent(data, time, color, comment);

        assertTrue(orderPage.checkSuccessOrderCreation());

    }

    @Test
    public void checkOrderUpButton() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickButtonOrderUp();
        OrderPage orderPage = new OrderPage(driver);
        assertTrue(orderPage.checkOpenOrderPage());
    }

    @Test
    public void checkOrderDownButton() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickButtonOrderDown();
        OrderPage orderPage = new OrderPage(driver);
        assertTrue(orderPage.checkOpenOrderPage());
    }

    @After
    public void browserClose() {
        driver.quit();
    }
}
