package com.leron.api.utils;

import com.leron.api.model.entities.BankMovement;
import com.leron.api.model.entities.Entrance;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GetStatusPayment {
    public static String getStatus(Entrance entrance, List<BankMovement> movements, int month, int year) {
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        if (!movements.isEmpty()) {
            LocalDate initialDate = entrance.getInitialDate().toLocalDateTime().toLocalDate();
            int monthFromDate = initialDate.getMonthValue();
            int yearFromDate = initialDate.getYear();
            if (monthFromDate == month && yearFromDate == year) {
                for (BankMovement bankMovement : movements) {
                    String[] part = bankMovement.getReferencePeriod().split("/");
                    int movementMonth = Integer.parseInt(part[0]);
                    int movementYear = Integer.parseInt(part[1]);

                    if (movementMonth == month && movementYear == year) {
                        if (entrance.getFrequency().equalsIgnoreCase("Mensal")) {
                            if (bankMovement.getType().equalsIgnoreCase("Entrada")) {
                                return "Confirmado";
                            }
                        } else if (entrance.getFrequency().equalsIgnoreCase("Única") ||
                                entrance.getFrequency().equalsIgnoreCase("Anual")) {
                            if (bankMovement.getType().equalsIgnoreCase("Entrada")) {
                                return "Confirmado";
                            }
                        } else if (entrance.getFrequency().equalsIgnoreCase("Trimestral")) {
                            ArrayList<ArrayList<Integer>> quarters = getArrayLists();
                            if (bankMovement.getType().equalsIgnoreCase("Entrada") &&
                                    belongsToSameQuarter(movementMonth, month, quarters)) {
                                return "Confirmado";
                            }
                        } else if (entrance.getFrequency().equalsIgnoreCase("Semestral")) {
                            ArrayList<ArrayList<Integer>> semesters = getArrayListsSemester();
                            if (bankMovement.getType().equalsIgnoreCase("Entrada") &&
                                    belongsToSameQuarter(movementMonth, month, semesters)) {
                                return "Confirmado";
                            }
                        }
                    }
                }
            } else {
                return "Não Inicada";
            }

            return "Pendente";
        } else {
            LocalDate initialDate = entrance.getInitialDate().toLocalDateTime().toLocalDate();
            int dayFromDate = initialDate.getDayOfMonth();
            int monthFromDate = initialDate.getMonthValue();
            int yearFromDate = initialDate.getYear();
            if (monthFromDate == month && yearFromDate == year) {
                if (entrance.getFrequency().equalsIgnoreCase("Mensal")) {
                    if (entrance.getDayReceive() >= currentDay && month == currentMonth && year == currentYear) {
                        return "Aguardando";
                    } else if (entrance.getDayReceive() <= dayFromDate && month == currentMonth && year == currentYear) {
                        return "Não Iniciada";
                    } else {
                        return "Pendente";
                    }
                } else if (entrance.getFrequency().equalsIgnoreCase("Única")) {
                    if (entrance.getInitialDate().after(Timestamp.valueOf(LocalDateTime.now().toLocalDate().atStartOfDay()))) {
                        return "Aguardando";
                    } else {
                        return "Pendente";
                    }
                } else if (entrance.getFrequency().equalsIgnoreCase("Anual")) {
                    if (entrance.getMonthReceive() == month) {
                        return "Aguardando";
                    } else {
                        return "Pendente";
                    }
                } else if (entrance.getFrequency().equalsIgnoreCase("Trimestral")) {
                    ArrayList<ArrayList<Integer>> quarters = getArrayLists();
                    if (belongsToSameQuarter(currentMonth, month, quarters)) {
                        return "Aguardando";
                    } else {
                        return "Pendente";
                    }
                } else if (entrance.getFrequency().equalsIgnoreCase("Semestral")) {
                    ArrayList<ArrayList<Integer>> semesters = getArrayListsSemester();
                    if (belongsToSameQuarter(currentMonth, month, semesters)) {
                        return "Aguardando";
                    } else {
                        return "Pendente";
                    }
                }
            } else {
                return "Não Iniciada";
            }
        }
        return "";
    }

    private static ArrayList<ArrayList<Integer>> getArrayLists() {
        ArrayList<ArrayList<Integer>> quarters = new ArrayList<>();
        ArrayList<Integer> quarter1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        ArrayList<Integer> quarter2 = new ArrayList<>(Arrays.asList(5, 6, 7, 8));
        ArrayList<Integer> quarter3 = new ArrayList<>(Arrays.asList(9, 10, 11, 12));
        quarters.add(quarter1);
        quarters.add(quarter2);
        quarters.add(quarter3);
        return quarters;
    }

    private static ArrayList<ArrayList<Integer>> getArrayListsSemester() {
        ArrayList<ArrayList<Integer>> quarters = new ArrayList<>();
        ArrayList<Integer> quarter1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        ArrayList<Integer> quarter2 = new ArrayList<>(Arrays.asList(7, 8, 9, 10, 11, 12));
        quarters.add(quarter1);
        quarters.add(quarter2);

        return quarters;
    }

    private static boolean belongsToSameQuarter(int a, int b, ArrayList<ArrayList<Integer>> quarters) {
        for (ArrayList<Integer> quarter : quarters) {
            if (quarter.contains(a) && quarter.contains(b)) {
                return true;
            }
        }
        return false;
    }
}
