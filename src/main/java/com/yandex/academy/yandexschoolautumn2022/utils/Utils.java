package com.yandex.academy.yandexschoolautumn2022.utils;

import com.yandex.academy.yandexschoolautumn2022.model.SystemItemImport;
import com.yandex.academy.yandexschoolautumn2022.model.SystemItemType;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static Boolean validateInputData(List<SystemItemImport> items) {
        Set<String> hs = items.stream().map(SystemItemImport::getId).collect(Collectors.toSet());
        if (hs.size() == items.size()) {
            for (SystemItemImport item : items) {
                if (item.getId() == null
                        || (item.getType() == SystemItemType.FOLDER && item.getUrl() != null)
                        || (item.getType() == SystemItemType.FILE && !(item.getUrl().length() <= 255))
                        || (item.getType() == SystemItemType.FOLDER && item.getSize() != null)
                        || (item.getType() == SystemItemType.FILE && (item.getSize() == null || item.getSize() <= 0))
                ) {
                    return false;
                }
            }

            return true;

        }
        else {
            return false;
        }
    }
}
