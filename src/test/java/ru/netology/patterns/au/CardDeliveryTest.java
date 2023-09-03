package ru.netology.patterns.au;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;


public class CardDeliveryTest {

    @BeforeAll
    public static void setUpAll() {

        Configuration.browser = "firefox";
    }

    @BeforeEach
    public void setUpEach() {

        open("http://localhost:9999");
    }

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void formCompleteSuccess() {
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + currentDate));
    }

    @Test
    void dateEarlier3DaysPlus() {
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(2, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void latinLettersInNameFailed() {
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Voronina Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void phoneNumberLengthFailed() {
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+7910964323");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void agreementBoxUncheckedFailed() {
        $("[data-test-id='city'] input").setValue("Хабаровск");
        String currentDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(currentDate);
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("button.button").click();
        $("[data-test-id='agreement'] .checkbox__text")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void cityAndDateFromDroppedListAdd3() {
        $("[data-test-id='city'] input").sendKeys("Ха");
        $$(".menu-item__control").findBy(text("Хабаровск")).click();
        $(".icon-button__text").click();
        int addDays = 3;
        String currentMonth = generateDate(0, "M");
        String DatePlus3 = generateDate(addDays, "M");
        String plannedDay = generateDate(addDays, "d");
        String compareDay = generateDate(addDays, "dd.MM.yyyy");
        if (!currentMonth.equals(DatePlus3)) {
            $("[data-step='1']").click();
        }
        $$("[data-day]").findBy(text(plannedDay)).click();
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + compareDay));
    }

    @Test
    void cityAndDateFromDroppedListAdd7() {
        $("[data-test-id='city'] input").sendKeys("Ха");
        $$(".menu-item__control").findBy(text("Хабаровск")).click();
        $(".icon-button__text").click();
        int addDays = 7;
        String currentMonth = generateDate(0, "M");
        String DatePlus7 = generateDate(addDays, "M");
        String plannedDay = generateDate(addDays, "d");
        String compareDay = generateDate(addDays, "dd.MM.yyyy");
        if (!currentMonth.equals(DatePlus7)) {
            $("[data-step='1']").click();
        }
        $$("[data-day]").findBy(text(plannedDay)).click();
        $("[data-test-id='name'] input").setValue("Воронина Елена");
        $("[data-test-id='phone'] input").setValue("+79109643232");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + compareDay));
    }
}