package com.leron.api.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

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

    public static Timestamp createTimestamp(int ano, int mes, int dia) {
        String dateString = String.format("%04d-%02d-%02d 00:00:00", ano, mes, dia);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date parsedDate = dateFormat.parse(dateString);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            return null;
        }

    }

    public static String getMonth(int month) {
        switch (month) {
            case 1:
                return "Janeiro";
            case 2:
                return "Fevereiro";
            case 3:
                return "Março";
            case 4:
                return "Abril";
            case 5:
                return "Maio";
            case 6:
                return "Junho";
            case 7:
                return "Julho";
            case 8:
                return "Agosto";
            case 9:
                return "Setembro";
            case 10:
                return "Outubro";
            case 11:
                return "Novembro";
            case 12:
                return "Dezembro";
            default:
                throw new IllegalArgumentException("Mês inválido: " + month);
        }
    }

    public static int getMonth(String monthName) {
        switch (monthName.toLowerCase()) {
            case "janeiro":
                return 1;
            case "fevereiro":
                return 2;
            case "março":
                return 3;
            case "abril":
                return 4;
            case "maio":
                return 5;
            case "junho":
                return 6;
            case "julho":
                return 7;
            case "agosto":
                return 8;
            case "setembro":
                return 9;
            case "outubro":
                return 10;
            case "novembro":
                return 11;
            case "dezembro":
                return 12;
            default:
                throw new IllegalArgumentException("Nome do mês inválido: " + monthName);
        }
    }


    public static ArrayList<String> populateMonths() {
        ArrayList<String> months = new ArrayList<>();
        months.add("Janeiro");
        months.add("Fevereiro");
        months.add("Março");
        months.add("Abril");
        months.add("Maio");
        months.add("Junho");
        months.add("Julho");
        months.add("Agosto");
        months.add("Setembro");
        months.add("Outubro");
        months.add("Novembro");
        months.add("Dezembro");
        return months;
    }

}
