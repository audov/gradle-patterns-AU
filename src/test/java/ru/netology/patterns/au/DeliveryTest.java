package ru.netology.patterns.au;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryTest {

    @BeforeAll
    public static void setUpAll() {

        Configuration.browser = "firefox";
        open("http://localhost:9999");
    }

    private Faker faker;

    @BeforeEach
    void setUpEach() {
        faker = new Faker(new Locale("ru"));
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id='city'] input").setValue("Хабаровск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Успешно!"));
        $(byXpath("//*[normalize-space(.)='Встреча успешно забронирована на ' and contains(@class 'notification__content')]"));
        //$("[data-test-id='success-notification'] .notification__content")
        //        .shouldHave(Condition.text(
        //                "Встреча успешно забронирована на "
        //                        + firstMeetingDate));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $("button.button").click();
        $(byXpath("//*[normalize-space(.)='У вас уже запланирована встреча на другую дату. Перепланировать?' and contains(@class 'notification__content')]"));
        //$("[data-test-id='replan-notification'] .notification__content")
        //        .shouldBe(Condition.visible, Duration.ofSeconds(15))
        //        .shouldHave(Condition.exactText("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id='replan-notification'] button").click();
        $(byXpath("//*[normalize-space(.)='Встреча успешно забронирована на ' and contains(@class 'notification__content')]"));
        //$("[data-test-id='replan-notification'] .notification__content")
        //        .shouldBe(Condition.visible, Duration.ofSeconds(15))
        //        .shouldHave(Condition.text("Встреча успешно забронирована на " + secondMeetingDate));
    }
}