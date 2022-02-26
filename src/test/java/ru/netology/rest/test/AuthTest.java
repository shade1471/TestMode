package ru.netology.rest.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.rest.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldAuthIfUserExistAndStatusActive() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("en", "active");
        $("[name=login]").setValue(user.getLogin());
        $("[name=password]").setValue(user.getPassword());
        $(byText("Продолжить")).click();
        $(".heading").shouldHave(text("  Личный кабинет"));
    }

    @Test
    void shouldAuthIfUserExistStatusActivePasswordNotCorrect() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("en", "active");
        $("[name=login]").setValue(user.getLogin());
        $("[name=password]").setValue("notCorrectPassword");
        $(byText("Продолжить")).click();
        $("[data-test-id] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldAuthIfUserExistStatusActiveAndLoginWithMistake() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("en", "active");
        $("[name=login]").setValue(user.getLogin() + "t");
        $("[name=password]").setValue(user.getPassword());
        $(byText("Продолжить")).click();
        $("[data-test-id] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldAuthIfUserExistStatusBlocked() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("en", "blocked");
        $("[name=login]").setValue(user.getLogin());
        $("[name=password]").setValue(user.getPassword());
        $(byText("Продолжить")).click();
        $("[data-test-id] .notification__content").shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldAuthIfUserBlockedAndStatusChangeToActive() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("en", "blocked");
        $("[name=login]").setValue(user.getLogin());
        $("[name=password]").setValue(user.getPassword());
        $(byText("Продолжить")).click();
        $("[data-test-id] .notification__content").shouldHave(text("Ошибка! Пользователь заблокирован"));

        DataGenerator.Registration.manualUser(user.getLogin(), user.getPassword(), "active");

        $(byText("Продолжить")).click();
        $(".heading").shouldHave(text("  Личный кабинет"));
    }

    @Test
    void shouldAuthIfUserNotExist() {
        $("[name=login]").setValue("chipolino.onion");
        $("[name=password]").setValue("dontcut");
        $(byText("Продолжить")).click();
        $("[data-test-id] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldAuthIfUserBlockedAndPasswordIncorrect() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("en", "blocked");

        $("[name=login]").setValue(user.getLogin());
        $("[name=password]").setValue(user.getPassword() + "mistake");
        $(byText("Продолжить")).click();
        $("[data-test-id] .notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

}
