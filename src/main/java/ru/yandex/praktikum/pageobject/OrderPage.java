package ru.yandex.praktikum.pageobject;
//Описание элементов и методов нужных для проведения позитивных тестов из сценария OrderTest.

import org.openqa.selenium.*;

public class OrderPage {
    //Нужны элементы страницы.
    //поля Имя.
    private static final By NAME_FIELD = By.xpath("//input[@placeholder = '* Имя']");
    //Поле Фамилия.
    private static final By LAST_NAME_FIELD = By.xpath("//input[@placeholder = '* Фамилия']");
    //Поле адрес.
    private static final By ADDRESS_FIELD = By.xpath("//input[@placeholder = '* Адрес: куда привезти заказ']");
    //Поле метро.
    private static final By METRO_STATION_FIELD = By.xpath("//input[@placeholder = '* Станция метро']");
    //Поле телефон.
    private static final By PHONE_FIELD = By.xpath("//input[@placeholder = '* Телефон: на него позвонит курьер']");

    //Поле дата привоза самоката
    private static final By DATE_FIELD = By.xpath("//input[@placeholder = '* Когда привезти самокат']");
    //Поле срок аренды
    private static final By PERIOD_FIELD = By.className("Dropdown-root");
    //Поле цвет самоката
    private static final By BLACK_COLOR_FIELD = By.xpath("//input[@id = 'black']/parent::label");
    private static final By GREY_COLOR_FIELD = By.xpath("//input[@id = 'grey']/parent::label");
    //Поле коментарий курьеру
    private static final By COMMENT_FIELD = By.xpath("//input[@placeholder = 'Комментарий для курьера']");
    //Сообщение что заказ оформлен.
    private static final By ORDER_PLACES = By.xpath("//div[contains(text(), 'Заказ оформлен')]");

    //Кнопка далеена первом экране.
    private  static final By NEXT_BUTTON = By.xpath("//button[@class = 'Button_Button__ra12g Button_Middle__1CSJM' and text() = 'Далее']");
    //Кнопка заказать на втором экране.
    private  static final By ORDER_BUTTON = By.xpath("//button[@class = 'Button_Button__ra12g Button_Middle__1CSJM' and text() = 'Заказать']");
    //Кнопка да потвердить заказ на третьем экране.
    private  static final By YES_ORDER_BUTTON = By.xpath("//button[@class = 'Button_Button__ra12g Button_Middle__1CSJM' and text() = 'Да']");

    private final WebDriver driver;
    //Конструктор объекта OrderPage с методами страницы,трех экранов и сообщение об успешном заказе.
    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }
    //Методы
    //Ввод Имени
    public  void enterName(String name) {
        driver.findElement(NAME_FIELD).sendKeys(name);
    }
    //Ввод Фамилии
    public  void enterLastName(String lastName) {
        driver.findElement(LAST_NAME_FIELD).sendKeys(lastName);
    }
    //Ввод адреса доставки
    public  void enterAddress(String address) {
        driver.findElement(ADDRESS_FIELD).sendKeys(address);
    }
    //Ввод ближайшего метро
    public void enterMetroStation(String metroStation) {
        WebElement input = driver.findElement(METRO_STATION_FIELD);
        input.click();
        input.clear();
        input.sendKeys(metroStation);

        // дождаться появления списка вариантов
        By anyOptionList = By.xpath("//*[contains(@class,'select-search__option') or contains(@class,'select-search__options')]");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(anyOptionList));

        // кликнуть ровно нужную станцию
        By exactOption = By.xpath("//button[.//div[normalize-space(text())='" + metroStation + "']]");
        WebElement option = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(exactOption));

        // на всякий — прокрутка к опции, затем клик
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", option);
        option.click();
    }
    //Ввод телефона
    public void enterPhone(String phone) {
        driver.findElement(PHONE_FIELD).sendKeys(phone);
    }
    //Нажатие кнопки далее первый экран заказа
    public void clickNextButton() {
        driver.findElement(NEXT_BUTTON).click();
    }
    //Ввод даты
    public void enterDateStart(String date) {
        driver.findElement(DATE_FIELD).sendKeys(date);
        driver.findElement(DATE_FIELD).sendKeys(Keys.ESCAPE); //нужно для сброса окна выбора даты
    }

    //Ввод срока аренды
    public void enterPeriod(String period) {
        driver.findElement(PERIOD_FIELD).click();
        driver.findElement(By.xpath("//div[@class = 'Dropdown-option' and text() = '" + period + "']")).click();
    }

    //Выбор цвета
    public void enterColor(String color) {
        String c = color.toLowerCase();
        if (c.contains("черн")) {
            driver.findElement(BLACK_COLOR_FIELD).click();
        } else if (c.contains("сер") || c.contains("grey") || c.contains("серая")) {
            driver.findElement(GREY_COLOR_FIELD).click();
        }
    }

    //Написать комментарий
    //Если параметр путое поле не трогаем
    public void enterComment(String comment) {
        if (!comment.isEmpty()) {
            driver.findElement(COMMENT_FIELD).sendKeys(comment);
        }
    }

    //Нажатие кнопки заказать ,второй экран заказа
    public void clickOrderButton() {
        driver.findElement(ORDER_BUTTON).click();
    }

    //Нажатие кнопки да, третий экран подтвердить заказ
    public void clickYesOrderButton() {
        driver.findElement(YES_ORDER_BUTTON).click();
    }

    //Метод проверки появилось ли окно подтверждения заказа.
    public boolean isSuccessfulOrderWindowDisplayed() {
        return new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(d -> !d.findElements(ORDER_PLACES).isEmpty());
    }
}
