package ru.yandex.praktikum.test;
//Параметрический тест. Тестовые данные представляют собой два массива с данными для заполнение форм и выбором точки входа.
//На последнем шаге в хроме выявлен баг с появлением окна с информацией о созданном заказе. В firefox все хорошо.


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.pageobject.OrderPage;


//Создадим класс с параметрическим записком.
@RunWith(Parameterized.class)
public class OrderTest extends ParentTest {
    //Переменые класса.
    private final String name;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String date;
    private final String period;
    private final String color;
    private final String comment;
    private final String entryPoint;

    public OrderTest(String name, String lastName, String address, String metroStation, String phone, String date, String period, String color, String comment, String entryPoint) {
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.date = date;
        this.period = period;
        this.color = color;
        this.comment = comment;
        this.entryPoint = entryPoint;
    }
    //Входные данные для позитивных сценариев. Коментарий может быть пустым, тогда поле комментария не будет кликаться.
    @Parameterized.Parameters(name = "Клиент: {0} {1}")
    public static Object[][] getData() {
        //Сгенерируем тестовые данные.
        return new Object[][] {
                {/*Имя*/ "Петя", /*Фамилия*/ "Иванов", /*Адрес Москва и Московская область*/ "Ул.Генерала Дорохова д.7", /*Метро*/ "Митино", /*телефон*/ "89856467564", /*дата начала*/ "31.07.2025", /*срок аренды*/ "двое суток", /*цвет самоката*/ "черный жемчуг", /*комметарий*/ "перед выездом позвонить", /*точка входа*/ "верх"},
                {"Вася", "Сидоров", "Ул. Беговая д.17", "Беговая", "89035436898", "23.10.2025", "трое суток", "серая безысходность", "звонить пока не возьму трубку", "низ"}

        };
    }
    //Прохождение позитивного пути нажатие одной из двух кнопок заказать.
    //Заполнения первой формы и нажатие далее, заполнение второй формы и нажатие заказать.
    //потверждение заказа в третьем окне кнопкой да.
    //Проверка что окно о создании заказа появилось.

    @Test
    public void createOrderPositiveDataOrderCreated() {
        //Главная страница
        mainPage.clickOrderButton(entryPoint);
        //Первая страница заказа
        OrderPage orderPage = new OrderPage(driver);
        orderPage.enterName(name);
        orderPage.enterLastName(lastName);
        orderPage.enterAddress(address);
        orderPage.enterMetroStation(metroStation);
        orderPage.enterPhone(phone);
        orderPage.clickNextButton();
        //Втарая страница заказа.
        orderPage.enterDateStart(date);
        orderPage.enterPeriod(period);
        orderPage.enterColor(color);
        orderPage.enterComment(comment);
        orderPage.clickOrderButton();
        //Третия страница
        orderPage.clickYesOrderButton();


        Assert.assertTrue(
                "Не найдено сообщение об успешном оформлении заказа для клиента " + name + " " + lastName,
                orderPage.isSuccessfulOrderWindowDisplayed()
        );
    }
}