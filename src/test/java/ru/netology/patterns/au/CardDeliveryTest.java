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
        $("[data-test-id='city'] input").setValue(DataGenerator.generateCity());
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        String plannedDate = DataGenerator.generateDate(4);
        $("[data-test-id='date'] input").setValue(plannedDate);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id='phone'] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + plannedDate));
    }

    @Test
    void dateEarlier3DaysPlus() {
        $("[data-test-id='city'] input").setValue(DataGenerator.generateCity());
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        String plannedDate = DataGenerator.generateDate(2);
        $("[data-test-id='date'] input").sendKeys(plannedDate);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id='phone'] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void latinLettersInNameFailed() {
        $("[data-test-id='city'] input").setValue(DataGenerator.generateCity());
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        String plannedDate = DataGenerator.generateDate(4);
        $("[data-test-id='date'] input").sendKeys(plannedDate);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateName("en"));
        $("[data-test-id='phone'] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void phoneNumberLengthFailed() {
        $("[data-test-id='city'] input").setValue(DataGenerator.generateCity());
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        String plannedDate = DataGenerator.generateDate(4);
        $("[data-test-id='date'] input").sendKeys(plannedDate);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id='phone'] input").setValue(DataGenerator.generateWrongPhone());
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void agreementBoxUncheckedFailed() {
        $("[data-test-id='city'] input").setValue(DataGenerator.generateCity());
        $("[data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        String plannedDate = DataGenerator.generateDate(4);
        $("[data-test-id='date'] input").sendKeys(plannedDate);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id='phone'] input").setValue(DataGenerator.generatePhone());
        $("button.button").click();
        $("[data-test-id='agreement'] .checkbox__text")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void cityAndDateFromDroppedListAdd3() {
        $("[data-test-id='city'] input").sendKeys("Мо");
        $$(".menu-item__control").findBy(text("Москва")).click();
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
        $("[data-test-id='name'] input").setValue("Воронина Анжела");
        $("[data-test-id='phone'] input").setValue("+79109643233");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + compareDay));
    }

    @Test
    void cityAndDateFromDroppedListAdd7() {
        $("[data-test-id='city'] input").sendKeys("Ка");
        $$(".menu-item__control").findBy(text("Казань")).click();
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
        $("[data-test-id='name'] input").setValue("Воронина Вера");
        $("[data-test-id='phone'] input").setValue("+79109643234");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + compareDay));
    }
}