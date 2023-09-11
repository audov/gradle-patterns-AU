package ru.netology.patterns.au;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {

    }

    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity() {
        var city = new String[]{"Москва", "Санкт-Петербург", "Мурманск", "Хабаровск", "Астрахань"};

        return city[new Random().nextInt(city.length)];
    }

    public static String generateName(String locale) {
        var faker = new Faker(new Locale(locale));

        return faker.name().fullName();
    }

    public static String generatePhone() {
        var phone = new String[]{"+79037109696", "+79057109696", "+79107109696"};

        return phone[new Random().nextInt(phone.length)];
    }

    public static String generateWrongPhone() {
        var phone = new String[]{"+7903710969", "+7905710969", "+7910710969"};

        return phone[new Random().nextInt(phone.length)];
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {

            return new UserInfo(generateCity(), generateName(locale), generatePhone());
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }
    }
}