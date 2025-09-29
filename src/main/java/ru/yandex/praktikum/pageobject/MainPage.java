package ru.yandex.praktikum.pageobject;
//Содержит метод для тестов из файлов OrderTest и QuestionTest.

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class MainPage {


//Стартовая сираница приложения.
private static final String PAGE_URL = "https://qa-scooter.praktikum-services.ru";
//Кнопка заказать в хэдере.
private static final By ORDER_HEADER_BUTTON = By.xpath("//div[@class = 'Header_Nav__AGCXC']/button[@class = 'Button_Button__ra12g']");
//Кнопка заказать в мэйне.
private static final By ORDER_HOME_BUTTON = By.xpath("//button[@class = 'Button_Button__ra12g Button_Middle__1CSJM']");
//Строка для вставки для поиска корневых элементов в FAQ.
private static final By ACCORDION_ITEM = By.xpath("//div[@class = 'accordion__item']");
//Кнопка принятия куки.
private static final By ACCEPT_COOKIES = By.id("rcc-confirm-button");
private final WebDriver driver;

public MainPage(WebDriver driver) {
    this.driver = driver;
}

//Открыть страницу и согласиться принять куки,что бы это окно не мешало.
public void pageOpen() {
        driver.get(PAGE_URL);

}

//Принять куки.

public void AcceptCookies() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    try {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(ACCEPT_COOKIES));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        btn.click();
        // дождаться, что баннер исчез
        wait.until(ExpectedConditions.invisibilityOfElementLocated(ACCEPT_COOKIES));
    } catch (TimeoutException e) {
        // баннера нет — ок
    } catch (ElementClickInterceptedException e) {
        // на редкий случай перекрытия — кликаем через JS и ждём исчезновения
        try {
            WebElement btn = driver.findElement(ACCEPT_COOKIES);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.invisibilityOfElementLocated(ACCEPT_COOKIES));
        } catch (Exception ignored) { }
    }
}

//Нажать на кнопку заказ , вверхнюю или нижнюю,в зависимости от внешнего параметра.
public void clickOrderButton(String entryPoint) {
    if (entryPoint.equals("низ")) {
        driver.findElement(ORDER_HOME_BUTTON).click();
    } else if (entryPoint.equals("верх")) {
        driver.findElement(ORDER_HEADER_BUTTON).click();
    }
}

//Нажатие на заголовок FAQ, передав номер элемента FAQ.
public void clickAccordionItemButton(int number_items) {
    By heading = By.id("accordion__heading-" + number_items);
    WebDriverWait w = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));
    WebElement h = w.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(heading));
    ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block:'center'});", h);
    w.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(h)).click();
}

// Текст вопроса
public String isQuestionTextDisplayed(int number_items) {
    By heading = By.id("accordion__heading-" + number_items);
    WebElement h = driver.findElement(heading);
    // На большинстве стендов текст лежит в .accordion__button
    java.util.List<WebElement> buttons = h.findElements(By.cssSelector(".accordion__button"));
    if (!buttons.isEmpty()) {
        return buttons.get(0).getText().trim();
    }
    // fallback: вдруг текст всё-таки в самом heading
    return h.getText().trim();
}

//Блок ответа отображается на экране
public boolean isBlockAnswerTextDisplayed(int number_items) {
    WebElement answerBlockTextDisplayed = driver.findElement(By.id("accordion__panel-"+number_items));
    return answerBlockTextDisplayed.isDisplayed();
}

//Наличие видимого текста (не пустая строка) в блоке ответа
public String isAnswerTextDisplayed(int number_items) {
    clickAccordionItemButton(number_items);
    By panel = By.id("accordion__panel-" + number_items);
    WebElement answer = new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(panel));
    return answer.getText();//getText - возвращает только для видимых объектов
}
// Получить количество вопросов в FAQ
public int qetQuestionsSize() {
    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(8));
    w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ACCORDION_ITEM));
    return driver.findElements(ACCORDION_ITEM).size();
}
}