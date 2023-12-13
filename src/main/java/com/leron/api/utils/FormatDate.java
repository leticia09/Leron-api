package com.leron.api.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FormatDate {
    public static Timestamp formatDate(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(outputFormatter);

        return Timestamp.valueOf(formattedDateTime);
    }
}
