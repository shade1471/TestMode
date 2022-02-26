package ru.netology.rest.data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    public DataGenerator() {
    }

    @Value
    public static class UserInfo {
        String login;
        String password;
        String status;
    }

    public static String generateLogin(String locale) {
        String login = new Faker(new Locale(locale)).name().username();
        return login;
    }

    public static String generatePassword(String locale) {
        String password = new Faker(new Locale(locale)).internet().password();
        return password;
    }

    public static String generateStatus() {
        String[] status = {"active", "blocked"};
        String stat = status[new Random().nextInt(status.length)];
        return stat;
    }

    public static class Registration {

        static RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        public Registration() {
        }

        public static UserInfo generateUser(String locale, String status) {
            UserInfo userGen = new UserInfo(DataGenerator.generateLogin(locale), DataGenerator.generatePassword(locale), status);
            given() // "дано"
                    .spec(requestSpec) // указываем, какую спецификацию используем
                    .body(new Gson().toJson(userGen)) // передаём в теле объект, который будет преобразован в JSON
                    .when() // "когда"
                    .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                    .then()
                    .statusCode(200);
            return userGen;
        }

        public static void manualUser(String login, String password, String status) {
            UserInfo user = new UserInfo(login, password, status);
            given() // "дано"
                    .spec(requestSpec) // указываем, какую спецификацию используем
                    .body(new Gson().toJson(user)) // передаём в теле объект, который будет преобразован в JSON
                    .when() // "когда"
                    .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                    .then()
                    .statusCode(200);
        }
    }
}
