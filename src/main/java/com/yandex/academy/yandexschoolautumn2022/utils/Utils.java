package com.yandex.academy.yandexschoolautumn2022.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {
    public static boolean isIsoDate(String date) {
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String removeOneDay(String date) {
        return OffsetDateTime.parse(date).minusDays(1).toString();
    }
}
