package ru.yandex.praktikum.pageobject;
//Содержит метод для тестов из файлов OrderTest и QuestionTest.

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


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
private static final By ACCORDION_BUTTON_IN_HEADING = By.cssSelector(".accordion__button");
private static final String ACCORDION_HEADING_ID_TPL = "accordion__heading-%d";
private static final String ACCORDION_PANEL_ID_TPL   = "accordion__panel-%d";

private final WebDriver driver;

public MainPage(WebDriver driver) {
    this.driver = driver;
}

    private By headingByIndex(int i) {
        return By.id(String.format(ACCORDION_HEADING_ID_TPL, i));
    }
    private By panelByIndex(int i) {
        return By.id(String.format(ACCORDION_PANEL_ID_TPL, i));
    }

//Открыть страницу и согласиться принять куки,что бы это окно не мешало.
public void pageOpen() {
        driver.get(PAGE_URL);
    new WebDriverWait(driver, Duration.ofSeconds(5)).until(
            ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(ORDER_HOME_BUTTON),
                    ExpectedConditions.presenceOfElementLocated(ORDER_HEADER_BUTTON),
                    ExpectedConditions.presenceOfElementLocated(ACCEPT_COOKIES)
            )
    );
}

//Принять куки.

public void acceptCookies() {
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
    By heading = headingByIndex(number_items);
    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(5));
    WebElement h = w.until(ExpectedConditions.presenceOfElementLocated(heading));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", h);
    try {
        w.until(ExpectedConditions.elementToBeClickable(h)).click();
    } catch (ElementClickInterceptedException e) {
        // если перекрыто картинкой → принудительный js-клик
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", h);
    }
}

// Текст вопроса
public String getQuestionText(int number_items) {
    WebElement h = driver.findElement(headingByIndex(number_items));
    List<WebElement> buttons = h.findElements(ACCORDION_BUTTON_IN_HEADING);

    if (!buttons.isEmpty()) {
        return buttons.get(0).getText().trim();
    }
    // fallback: вдруг текст всё-таки в самом heading
    return h.getText().trim();
}

//Блок ответа отображается на экране
public boolean isBlockAnswerTextDisplayed(int number_items) {
    WebElement answerBlock = driver.findElement(panelByIndex(number_items));
    return answerBlock.isDisplayed();
}

//Наличие видимого текста (не пустая строка) в блоке ответа
public String getAnswerText(int number_items) {
    clickAccordionItemButton(number_items);
    By panel = panelByIndex(number_items);
    WebElement answer = new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(panel));
    return answer.getText(); //getText - возвращает только для видимых объектов
}
// Получить количество вопросов в FAQ
public int getQuestionsSize() {
    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(8));
    w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ACCORDION_ITEM));
    return driver.findElements(ACCORDION_ITEM).size();
}
}