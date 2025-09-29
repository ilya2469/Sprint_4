package ru.yandex.praktikum.test;
//Выпадающий список в разделе «Вопросы о важном». Тебе нужно проверить: когда нажимаешь на стрелочку, открывается соответствующий текст.

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class QuestionTest extends ParentTest {
    private final int position;
    private final String question;
    private final String answer;
    //Мы не можем посмотреть количество тестовых данных, поэтому передадим их количество в ручную.
    private static final int COUNT_ANSWER = 8;

    public QuestionTest(int position, String question, String answer) {
        this.position = position;
        this.question = question;
        this.answer = answer;
    }

    // Входные данные для позитивных сценариев. Комментарий может быть пустым, тогда поле коментария не будет кликаться.
    @Parameterized.Parameters(name = "вопрос номер: {0}")
    public static Object[][] getData() {
        //Сгенерируй тестовые данные. Нам нужно название городов и результат поиска.
    return new Object[][] {
            {0, "Сколько это стоит? И как оплатить?", "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
            {1, "Хочу сразу несколько самокатов! Так можно?", "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
            {2, "Как рассчитывается время аренды?", "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
            {3, "Можно ли заказать самокат прямо на сегодня?", "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
            {4, "Можно ли продлить заказ или вернуть самокат раньше?", "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
            {5, "Вы привозите зарядку вместе с самокатом?", "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
            {6, "Можно ли отменить заказ?", "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
            {7, "Я жизу за МКАДом, привезёте?", "Да, обязательно. Всем самокатов! И Москве, и Московской области."}
    };
    }
    //Проверим, что текст вопросов соответствует ожидаемым
    @Test
    public void expandAccordionQuestionDisplayedQuestionValid() {
        Assert.assertEquals("Ожидаем что блок ответа скрыт для вопроса №" + position, question, mainPage.isQuestionTextDisplayed(position));
    }
    //Проверим, что до нажатия на карточку ответа нет
    @Test
    public void expandAccordionItemsNotClickItemCollapse() {
        Assert.assertFalse("Ожидаем что блок ответа скрыт для вопроса №" + position, mainPage.isBlockAnswerTextDisplayed(position));
    }
    //Проверим, что при клике ответ появился на экране и соответствует ожидаемому.
    @Test
    public void expandAccordionItemsClickItemExpand() {
        Assert.assertEquals("Ожидаем что текст ответа появился после клика и соответствует ожидаемому для вопроса №" + position, answer, mainPage.isAnswerTextDisplayed(position));
    }
    //Проверим, что на сайте нет еще вопросов которые мы забыли описать.
    @Test
    public void expandAccordionItemsDisplayedCountItemsValid() {
        Assert.assertEquals("Число тестовых данных не совпадает с количеством вопросов на сайте. Проверь константу COUNT_ANSWER", COUNT_ANSWER, mainPage.qetQuestionsSize());
    }
}
