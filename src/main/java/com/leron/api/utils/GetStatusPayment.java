package com.leron.api.utils;

import com.leron.api.model.entities.BankMovement;
import com.leron.api.model.entities.Entrance;
import com.leron.api.model.entities.Expense;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class GetStatusPayment {
    public static String getStatus(Entrance entrance, List<BankMovement> movements, int month, int year) {
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        LocalDate initialDate = entrance.getInitialDate().toLocalDateTime().toLocalDate();
        int initialDay = initialDate.getDayOfMonth();
        int initialMonth = initialDate.getMonthValue();
        int initialYear = initialDate.getYear();
        Timestamp date = FormatDate.createTimestamp(year, month, initialDay);

        if (!movements.isEmpty()) {

            for (BankMovement bankMovement : movements) {
                String[] part = bankMovement.getReferencePeriod().split("/");
                int movementMonth = Integer.parseInt(part[0]);
                int movementYear = Integer.parseInt(part[1]);

                if (entrance.getFrequency().equalsIgnoreCase("Mensal")) {
                    if (entrance.getInitialDate().after(date)) {
                        return "Não Iniciada";
                    } else if (bankMovement.getType().equalsIgnoreCase("Entrada") && movementMonth == month && movementYear == year) {
                        return "Confirmado";
                    } else if (entrance.getDayReceive() < currentDay && currentMonth == month) {
                        return "Pendente";
                    } else {
                        return "Aguardando";
                    }
                } else if (entrance.getFrequency().equalsIgnoreCase("Única")) {
                    if (entrance.getInitialDate().after(date)) {
                        return "Não Iniciada";
                    } else if (bankMovement.getType().equalsIgnoreCase("Entrada") && movementMonth == month && movementYear == year) {
                        return "Confirmado";
                    } else if (entrance.getInitialDate().after(new Date())) {
                        return "Pendente";
                    } else {
                        return "Aguardando";
                    }
                } else if (entrance.getFrequency().equalsIgnoreCase("Anual")) {
                    if (entrance.getInitialDate().after(date) || entrance.getMonthReceive() >= month) {
                        return "Não Iniciada";
                    } else if (bankMovement.getType().equalsIgnoreCase("Entrada") && movementMonth == month && movementYear == year) {
                        return "Confirmado";
                    } else if (entrance.getInitialDate().after(new Date())) {
                        return "Pendente";
                    } else {
                        return "Aguardando";
                    }
                } else if (entrance.getFrequency().equalsIgnoreCase("Trimestral")) {
                    ArrayList<ArrayList<Integer>> quarters = getArrayLists();
                    if (entrance.getInitialDate().after(date)) {
                        return "Não Iniciada";
                    } else if (bankMovement.getType().equalsIgnoreCase("Entrada") &&
                            belongsToSameQuarter(movementMonth, month, quarters)) {
                        return "Confirmado";
                    }
                } else if (entrance.getFrequency().equalsIgnoreCase("Semestral")) {
                    ArrayList<ArrayList<Integer>> semesters = getArrayListsSemester();
                    if (entrance.getInitialDate().after(date)) {
                        return "Não Iniciada";
                    } else if (bankMovement.getType().equalsIgnoreCase("Entrada") &&
                            belongsToSameQuarter(movementMonth, month, semesters)) {
                        return "Confirmado";
                    }
                }
            }

        } else {
            if (entrance.getFrequency().equalsIgnoreCase("Mensal")) {
                if (entrance.getInitialDate().after(date)) {
                    return "Não Iniciada";
                } else if (entrance.getDayReceive() >= currentDay && month >= currentMonth && year >= currentYear) {
                    return "Aguardando";
                } else {
                    return "Pendente";
                }
            } else if (entrance.getFrequency().equalsIgnoreCase("Única")) {
                if (entrance.getInitialDate().after(date)) {
                    return "Não Iniciada";
                } else if (entrance.getInitialDate().after(Timestamp.valueOf(LocalDateTime.now())) && month == initialMonth && year == initialYear) {
                    return "Aguardando";
                } else {
                    return "Pendente";
                }
            } else if (entrance.getFrequency().equalsIgnoreCase("Anual")) {
                if (entrance.getInitialDate().after(date) || entrance.getMonthReceive() != month) {
                    return "Não Iniciada";
                } else if (entrance.getMonthReceive() == month) {
                    if (entrance.getDayReceive() < currentDay) {
                        return "Pendente";
                    } else {
                        return "Aguardando";
                    }
                }
            } else if (entrance.getFrequency().equalsIgnoreCase("Trimestral")) {
                ArrayList<ArrayList<Integer>> quarters = getArrayLists();
                if (entrance.getInitialDate().after(date)) {
                    return "Não Iniciada";
                } else if (belongsToSameQuarter(currentMonth, month, quarters)) {
                    return "Aguardando";
                } else {
                    return "Pendente";
                }
            } else if (entrance.getFrequency().equalsIgnoreCase("Semestral")) {
                ArrayList<ArrayList<Integer>> semesters = getArrayListsSemester();
                if (entrance.getInitialDate().after(date)) {
                    return "Não Iniciada";
                } else if (belongsToSameQuarter(currentMonth, month, semesters)) {
                    return "Aguardando";
                } else {
                    return "Pendente";
                }
            }

        }
        return "";
    }

    public static String getStatus(Expense expense, List<BankMovement> movements, int month, int year) {
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        LocalDate initialDate;
        if (Objects.nonNull(expense.getInitialDate())) {
            initialDate = expense.getInitialDate().toLocalDateTime().toLocalDate();
        } else {
            initialDate = expense.getDateBuy().toLocalDateTime().toLocalDate();
        }
        int initialDay = initialDate.getDayOfMonth();
        int initialMonth = initialDate.getMonthValue();
        int initialYear = initialDate.getYear();
        Timestamp date = FormatDate.createTimestamp(year, month, initialDay);

        if (!movements.isEmpty()) {

            for (BankMovement bankMovement : movements) {
                String[] part = bankMovement.getReferencePeriod().split("/");
                int movementMonth = Integer.parseInt(part[0]);
                int movementYear = Integer.parseInt(part[1]);
                if (expense.getHasSplitExpense()) {
                    if (expense.getFrequency().equalsIgnoreCase("Mensal")) {
                        if (expense.getInitialDate().after(date)) {
                            return "Não Iniciada";
                        } else if (bankMovement.getType().equalsIgnoreCase("Saída") && movementMonth == month && movementYear == year) {
                            return "Confirmado";
                        } else if (expense.getDayPayment() < currentDay && currentMonth == month) {
                            return "Pendente";
                        } else {
                            return "Aguardando";
                        }
                    } else if (expense.getFrequency().equalsIgnoreCase("Anual")) {
                        if (expense.getInitialDate().after(date) || expense.getMonthPayment() >= month) {
                            return "Não Iniciada";
                        } else if (bankMovement.getType().equalsIgnoreCase("Saída") && movementMonth == month && movementYear == year) {
                            return "Confirmado";
                        } else if (expense.getInitialDate().after(new Date())) {
                            return "Pendente";
                        } else {
                            return "Aguardando";
                        }
                    }
                } else if (expense.getHasFixed()) {
                    if (expense.getInitialDate().after(date)) {
                        return "Não Iniciada";
                    }  else if (bankMovement.getType().equalsIgnoreCase("Saída") && movementMonth == month && movementYear == year) {
                        return "Confirmado";
                    } else if (expense.getDayPayment() <= initialDay) {
                        return "Aguardando";
                    } else {
                        return "Pendente";
                    }
                } else {
                    if (expense.getDateBuy().after(date)) {
                        return "Não Iniciada";
                    } else if (bankMovement.getType().equalsIgnoreCase("Saída") && movementMonth == month && movementYear == year) {
                        return "Confirmado";
                    }
                }
            }

        } else {
            if (expense.getHasSplitExpense()) {
                int part = month - expense.getInitialDate().toLocalDateTime().toLocalDate().getMonthValue() + 1;
                if (expense.getFrequency().equalsIgnoreCase("Mensal")) {
                    if (part <= expense.getQuantityPart()) {
                        if (expense.getInitialDate().after(date)) {
                            return "Não Iniciada";
                        } else if (expense.getDayPayment() >= currentDay && month >= currentMonth && year >= currentYear) {
                            return "Aguardando";
                        } else {
                            return "Pendente";
                        }
                    }
                } else if (expense.getFrequency().equalsIgnoreCase("Anual")) {
                    if (expense.getInitialDate().after(date) || expense.getMonthPayment() != month) {
                        return "Não Iniciada";
                    } else if (expense.getMonthPayment() == month) {
                        if (expense.getDayPayment() < currentDay) {
                            return "Pendente";
                        } else {
                            return "Aguardando";
                        }
                    }
                }
            } else if (expense.getHasFixed()) {
                if (expense.getInitialDate().after(date)) {
                    return "Não Iniciada";
                } else if (expense.getDayPayment() >= currentDay) {
                    return "Aguardando";
                } else {
                    return "Pendente";
                }

            } else {
                if (expense.getDateBuy().after(date)) {
                    return "Não Iniciada";
                }
            }


        }
        return "";
    }

//    public static String getStatus(Expense expense, List<BankMovement> movements, int month, int year) {
//        LocalDate currentDate = LocalDate.now();
//        int currentDay = currentDate.getDayOfMonth();
//        int currentMonth = currentDate.getMonthValue();
//        int currentYear = currentDate.getYear();
//
//        if (!movements.isEmpty()) {
//            LocalDate initialDate = expense.getDateBuy().toLocalDateTime().toLocalDate();
//            int monthFromDate = initialDate.getMonthValue();
//            int yearFromDate = initialDate.getYear();
//            if (monthFromDate == month && yearFromDate == year) {
//                for (BankMovement bankMovement : movements) {
//                    String[] part = bankMovement.getReferencePeriod().split("/");
//                    int movementMonth = Integer.parseInt(part[0]);
//                    int movementYear = Integer.parseInt(part[1]);
//
//                    if (movementMonth == month && movementYear == year) {
//                        if (expense.getHasSplitExpense() || expense.getHasFixed()) {
//                            if (bankMovement.getType().equalsIgnoreCase("Saída")) {
//                                return "Confirmado";
//                            }
//
//                        } else {
//                            if (bankMovement.getType().equalsIgnoreCase("Saída")) {
//                                return "Confirmado";
//                            }
//                        }
//
//                    }
//                }
//            } else {
//                return "Não Iniciada";
//            }
//
//            return "Pendente";
//        } else {
//            LocalDate initialDate;
//            if(Objects.nonNull(expense.getDateBuy())){
//                initialDate = expense.getDateBuy().toLocalDateTime().toLocalDate();
//            } else {
//                initialDate = expense.getInitialDate().toLocalDateTime().toLocalDate();
//            }
//
//            int dayFromDate = initialDate.getDayOfMonth();
//            int monthFromDate = initialDate.getMonthValue();
//            int yearFromDate = initialDate.getYear();
//            if (monthFromDate == month && yearFromDate == year) {
//                if (expense.getHasSplitExpense() || expense.getHasFixed()) {
//                    if (Objects.nonNull(expense.getFrequency())) {
//                        if (expense.getFrequency().equalsIgnoreCase("mensal")) {
//                            if (expense.getDayPayment() >= currentDay && month == currentMonth && year == currentYear) {
//                                return "Aguardando";
//                            } else if (expense.getDayPayment() <= dayFromDate && month == currentMonth && year == currentYear) {
//                                return "Não Iniciada";
//                            } else {
//                                return "Pendente";
//                            }
//                        } else if (expense.getFrequency().equalsIgnoreCase("Única")) {
//                            if (expense.getInitialDate().after(Timestamp.valueOf(LocalDateTime.now().toLocalDate().atStartOfDay()))) {
//                                return "Aguardando";
//                            } else {
//                                return "Pendente";
//                            }
//                        } else if (expense.getFrequency().equalsIgnoreCase("Anual")) {
//                            if (expense.getMonthPayment() == month) {
//                                return "Aguardando";
//                            } else {
//                                return "Pendente";
//                            }
//                        }
//
//                    } else {
//                        if (expense.getDayPayment() <= dayFromDate) {
//                            return "Aguardando";
//                        } else {
//                            return "Pendente";
//                        }
//                    }
//
//
//                }
//            } else {
//                return "Não Iniciada";
//            }
//        }
//        return "";
//    }

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
