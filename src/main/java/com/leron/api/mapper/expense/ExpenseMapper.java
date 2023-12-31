package com.leron.api.mapper.expense;

import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.entities.*;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatDate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExpenseMapper {

    public static List<Expense> requestToEntity(List<ExpenseRequest> requestList, Account account) {
        List<Expense> response = new ArrayList<>();

        requestList.forEach(res -> {
            Expense expense = new Expense();
            expense.setAccountId(account.getId());
            expense.setBankId(account.getBank().getId());
            expense.setLocal(res.getLocal());
            expense.setMacroGroup(res.getMacroGroup());
            expense.setOwnerId(res.getOwnerId());
            expense.setPaymentForm(res.getPaymentForm());
            expense.setHasFixed(res.getHasFixed());
            expense.setDateBuy(FormatDate.formatDate((res.getDateBuy())));
            expense.setUserAuthId(res.getUserAuthId());
            expense.setValue(new BigDecimal(res.getValue().replace(",", ".")));
            expense.setCreatedIn(new Date());
            expense.setDeleted(false);
            if (Objects.nonNull(res.getFinalCard())) {
                expense.setFinalCard(res.getFinalCard());
            }
            if (Objects.nonNull(res.getQuantityPart())) {
                expense.setQuantityPart(res.getQuantityPart());
            }
            if (Objects.nonNull(res.getObs())) {
                expense.setObs(res.getObs());
            }

            response.add(expense);
        });

        return response;
    }

    public static Expense requestToEntity(ExpenseRequest res, Account account) {
        Expense expense = new Expense();
        expense.setAccountId(account.getId());
        expense.setBankId(account.getBank().getId());
        expense.setLocal(res.getLocal());
        expense.setMacroGroup(res.getMacroGroup());
        expense.setOwnerId(res.getOwnerId());
        expense.setPaymentForm(res.getPaymentForm());
        expense.setHasFixed(res.getHasFixed());
        expense.setDateBuy(FormatDate.formatDate((res.getDateBuy())));
        expense.setUserAuthId(res.getUserAuthId());
        expense.setValue(new BigDecimal(res.getValue().replace(",", ".")));
        expense.setCreatedIn(new Date());
        expense.setDeleted(false);
        expense.setHasSplitExpense(res.getHasSplitExpense());
        if (Objects.nonNull(res.getFinalCard())) {
            expense.setFinalCard(res.getFinalCard());
        }
        if (Objects.nonNull(res.getQuantityPart())) {
            expense.setQuantityPart(res.getQuantityPart());
        }
        if (Objects.nonNull(res.getObs())) {
            expense.setObs(res.getObs());
        }
        if (Objects.nonNull(res.getSpecificGroup())) {
            expense.setSpecificGroup(res.getSpecificGroup());
        }

        return expense;
    }

    public static Expense requestToEntityList(ExpenseRequest res, List<Card> cards) {
        Expense expense = new Expense();
        if (Objects.nonNull(res.getFinalCard())) {
            Optional<Card> cardOptional = cards.stream().filter(c -> c.getFinalNumber().equals(res.getFinalCard())).findFirst();
            if (cardOptional.isPresent()) {
                expense.setAccountId(cardOptional.get().getAccount().getId());
                expense.setBankId(cardOptional.get().getAccount().getBank().getId());
            }

        } else {
            expense.setAccountId(res.getAccountId());
            expense.setBankId(res.getBankId());
        }
        expense.setLocal(res.getLocal());
        expense.setMacroGroup(res.getMacroGroup());
        expense.setOwnerId(res.getOwnerId());
        expense.setPaymentForm(res.getPaymentForm());
        expense.setHasFixed(res.getHasFixed());
        if (Objects.nonNull(res.getDateBuy())) {
            expense.setDateBuy(FormatDate.formatDate((res.getDateBuy())));
        }

        expense.setUserAuthId(res.getUserAuthId());
        expense.setValue(new BigDecimal(res.getValue().replace(",", ".")));
        expense.setCreatedIn(new Date());
        expense.setDeleted(false);
        expense.setHasSplitExpense(res.getHasSplitExpense());

        if (Objects.nonNull(res.getFinalCard())) {
            expense.setFinalCard(res.getFinalCard());
        }
        if (Objects.nonNull(res.getQuantityPart())) {
            expense.setQuantityPart(res.getQuantityPart());
        }
        if (Objects.nonNull(res.getObs())) {
            expense.setObs(res.getObs());
        }
        if (Objects.nonNull(res.getSpecificGroup())) {
            expense.setSpecificGroup(res.getSpecificGroup());
        }

        if (Objects.nonNull(res.getFrequency())) {
            expense.setFrequency(res.getFrequency());
        }

        if (Objects.nonNull(res.getInitialDate())) {
            expense.setInitialDate(FormatDate.formatDate(res.getInitialDate()));
        }

        if (Objects.nonNull(res.getMonthPayment())) {
            expense.setMonthPayment(res.getMonthPayment());
        }

        if (Objects.nonNull(res.getDayPayment())) {
            expense.setDayPayment(res.getDayPayment());
        }

        if (Objects.nonNull(res.getMoneyId())) {
            expense.setMoneyId(res.getMoneyId());
        }

        if (Objects.nonNull(res.getTicketId())) {
            expense.setFinancialEntityId(res.getTicketId());
        }

        if (Objects.nonNull(res.getCardId())) {
            expense.setFinancialEntityCardId(res.getCardId());
        }


        return expense;
    }

    public static Expense requestToEntity(ExpenseRequest res) {
        Expense expense = new Expense();
        expense.setAccountId(res.getAccountId());
        expense.setBankId(res.getBankId());
        expense.setLocal(res.getLocal());
        expense.setMacroGroup(res.getMacroGroup());
        expense.setOwnerId(res.getOwnerId());
        expense.setPaymentForm(res.getPaymentForm());
        expense.setHasFixed(res.getHasFixed());
        if (Objects.nonNull(res.getDateBuy())) {
            expense.setDateBuy(FormatDate.formatDate((res.getDateBuy())));
        }

        expense.setUserAuthId(res.getUserAuthId());
        expense.setValue(new BigDecimal(res.getValue().replace(",", ".")));
        expense.setCreatedIn(new Date());
        expense.setDeleted(false);
        expense.setHasSplitExpense(res.getHasSplitExpense());

        if (Objects.nonNull(res.getFinalCard())) {
            expense.setFinalCard(res.getFinalCard());
        }
        if (Objects.nonNull(res.getQuantityPart())) {
            expense.setQuantityPart(res.getQuantityPart());
        }
        if (Objects.nonNull(res.getObs())) {
            expense.setObs(res.getObs());
        }
        if (Objects.nonNull(res.getSpecificGroup())) {
            expense.setSpecificGroup(res.getSpecificGroup());
        }

        if (Objects.nonNull(res.getFrequency())) {
            expense.setFrequency(res.getFrequency());
        }

        if (Objects.nonNull(res.getInitialDate())) {
            expense.setInitialDate(FormatDate.formatDate(res.getInitialDate()));
        }

        if (Objects.nonNull(res.getMonthPayment())) {
            expense.setMonthPayment(res.getMonthPayment());
        }

        if (Objects.nonNull(res.getDayPayment())) {
            expense.setDayPayment(res.getDayPayment());
        }

        if (Objects.nonNull(res.getMoneyId())) {
            expense.setMoneyId(res.getMoneyId());
        }

        if (Objects.nonNull(res.getTicketId())) {
            expense.setFinancialEntityId(res.getTicketId());
        }

        if (Objects.nonNull(res.getCardId())) {
            expense.setFinancialEntityCardId(res.getCardId());
        }


        return expense;
    }

    public static DataListResponse<ExpenseResponse> entityToResponse(List<Expense> expenses, List<Member> members, List<Card> cards, List<BankMovement> bankMovements, int month, int year, List<Account> accounts, List<Money> moneyList, List<CardFinancialEntity> cardFinancialEntityList) {
        DataListResponse<ExpenseResponse> response = new DataListResponse<>();
        List<ExpenseResponse> expenseList = new ArrayList<>();
        for (Expense expense : expenses) {
            LocalDate dateBuy;
            if (Objects.nonNull(expense.getInitialDate())) {
                dateBuy = expense.getInitialDate().toLocalDateTime().toLocalDate();
            } else {
                dateBuy = expense.getDateBuy().toLocalDateTime().toLocalDate();
            }


            ExpenseResponse expenseResponse = new ExpenseResponse();
            expenseResponse.setId(expense.getId());
            expenseResponse.setLocal(expense.getLocal());
            expenseResponse.setMacroGroup(expense.getMacroGroup());
            expenseResponse.setOwnerId(expense.getOwnerId());
            expenseResponse.setPaymentForm(expense.getPaymentForm());
            expenseResponse.setHasFixed(expense.getHasFixed());
            expenseResponse.setDateBuy(expense.getDateBuy());
            expenseResponse.setValue(expense.getValue());
            if (Objects.nonNull(expense.getObs())) {
                expenseResponse.setObs(expense.getObs());
            }

            if (Objects.nonNull(expense.getQuantityPart())) {
                expenseResponse.setQuantityPart(expense.getQuantityPart());
            }
            if (Objects.nonNull(expense.getSpecificGroup())) {
                expenseResponse.setSpecificGroup(expense.getSpecificGroup());
            }

            if (Objects.nonNull(expense.getMoneyId())) {
                Optional<Money> money = moneyList.stream().filter(m -> m.getId().equals(expense.getMoneyId())).findFirst();
                money.ifPresent(value -> expenseResponse.setCurrency(value.getCurrency()));
            }

            if (Objects.nonNull(expense.getFinancialEntityCardId())) {
                Optional<CardFinancialEntity> card = cardFinancialEntityList.stream().filter(c -> c.getId().equals(expense.getFinancialEntityCardId())).findFirst();
                card.ifPresent(value -> expenseResponse.setCurrency(value.getCurrency()));
            }

            if (Objects.nonNull(expense.getAccountId())) {
                Optional<Account> account = accounts.stream().filter(a -> a.getId().equals(expense.getAccountId())).findFirst();
                account.ifPresent(value -> expenseResponse.setCurrency(value.getCurrency()));
            }

            Optional<BankMovement> bankMovement = bankMovements.stream().filter(bm -> Objects.nonNull(bm.getExpenseId()) && bm.getExpenseId().equals(expense.getId())).findFirst();
            Optional<Card> cardOptional = cards.stream().filter(ca -> ca.getFinalNumber().equals(expense.getFinalCard())).findFirst();
            List<BankMovement> bankMovementList = bankMovements.stream().filter(bm -> Objects.nonNull(bm.getExpenseId()) && bm.getExpenseId().equals(expense.getId())).collect(Collectors.toList());

            cardOptional.ifPresent(card -> expenseResponse.setFinalCard(card.getName() + "/ " + expense.getFinalCard()));

            if (bankMovement.isPresent()) {
                if (!expense.getHasSplitExpense() && !expense.getHasFixed() && !expense.getPaymentForm().equalsIgnoreCase("crédito")) {
                    expenseResponse.setStatus("Confirmado");
                }

                if (expense.getPaymentForm().equalsIgnoreCase("crédito") || expense.getHasSplitExpense()) {
                    String[] part = bankMovement.get().getReferencePeriod().split("/");

                    int monthReferencePeriod = Integer.parseInt(part[0]);
                    int yearReferencePeriod = Integer.parseInt(part[1]);

                    if (yearReferencePeriod == year && monthReferencePeriod == month) {
                        expenseResponse.setStatus("Confirmado");
                        expenseResponse.setPartValue(bankMovement.get().getValue());
                        expenseResponse.setPartNumber(bankMovementList.size());
                    }
                }

            } else {
                LocalDate today = LocalDate.now();

                if (expense.getPaymentForm().equalsIgnoreCase("crédito")) {
                    if (cardOptional.isPresent() && cardOptional.get().getClosingDate() < dateBuy.getDayOfMonth()) {
                        expenseResponse.setStatus("Aguardando");
                    } else {
                        expenseResponse.setStatus("Pendente");
                    }
                }

                if ((expense.getPaymentForm().equalsIgnoreCase("débito") ||
                        expense.getPaymentForm().equalsIgnoreCase("pix") ||
                        expense.getPaymentForm().equalsIgnoreCase("dinheiro")) && (expense.getHasFixed() || expense.getHasSplitExpense())) {

                    if (expense.getHasFixed()) {
                        if (dateBuy.getYear() == year && dateBuy.getMonthValue() <= month) {
                            if (expense.getDayPayment() <= today.getDayOfMonth()) {
                                expenseResponse.setStatus("Aguardando");
                            } else {
                                expenseResponse.setStatus("Pendente");
                            }
                        } else {
                            expenseResponse.setStatus("Não Iniciada");
                        }
                    } else {
                        if (expense.getHasSplitExpense()) {
                            int monthFinished = dateBuy.getMonthValue() + Math.toIntExact(expense.getQuantityPart() -1);
                            int part = month - dateBuy.getMonthValue() + 1;
                            expenseResponse.setPartNumber(part);
                            expenseResponse.setPartValue(expense.getValue().divide(new BigDecimal(expense.getQuantityPart())));

                            if (month <= monthFinished) {
                                if (expense.getDayPayment() <= today.getDayOfMonth()) {
                                    expenseResponse.setStatus("Aguardando");
                                } else {
                                    expenseResponse.setStatus("Pendente");
                                }
                            } else {
                                expenseResponse.setStatus("Fechada");
                                expenseResponse.setPartNumber(Math.toIntExact(expense.getQuantityPart()));
                            }
                        } else {
                            if (dateBuy.getYear() == year && dateBuy.getMonthValue() == month) {
                                if (expense.getDayPayment() <= today.getDayOfMonth()) {
                                    expenseResponse.setStatus("Aguardando");
                                } else {
                                    expenseResponse.setStatus("Pendente");
                                }
                            } else {
                                expenseResponse.setStatus("Não Iniciada");
                            }

                        }
                    }
                }

            }

            if(!expense.getHasFixed() && !expense.getHasSplitExpense() && dateBuy.getYear() == year && dateBuy.getMonthValue() == month) {
                expenseList.add(expenseResponse);
            } else if((expense.getHasFixed() || expense.getHasSplitExpense()) && dateBuy.getYear() <= year && dateBuy.getMonthValue() <= month){
                expenseList.add(expenseResponse);
            }


        }
        response.setData(expenseList.stream().filter(expense -> !expense.getStatus().equalsIgnoreCase("Não Iniciada") && !expense.getStatus().equalsIgnoreCase("Fechada")).collect(Collectors.toList()));
        return response;
    }

    public static DataListResponse<ExpenseResponse> entityToResponse(List<Expense> expenses, List<Member> members, List<Bank> banks, List<BankMovement> bankMovements) {
        DataListResponse<ExpenseResponse> response = new DataListResponse<>();
        List<ExpenseResponse> expenseList = new ArrayList<>();
        for (Expense expense : expenses) {
            ExpenseResponse expenseResponse = new ExpenseResponse();
            expenseResponse.setId(expense.getId());
            expenseResponse.setLocal(expense.getLocal());
            expenseResponse.setMacroGroup(expense.getMacroGroup());
            expenseResponse.setOwnerId(expense.getOwnerId());
            expenseResponse.setPaymentForm(expense.getPaymentForm());
            expenseResponse.setHasFixed(expense.getHasFixed());
            expenseResponse.setDateBuy(expense.getDateBuy());
            expenseResponse.setValue(expense.getValue());
            if (Objects.nonNull(expense.getObs())) {
                expenseResponse.setObs(expense.getObs());
            }
            if (Objects.nonNull(expense.getFinalCard())) {
                expenseResponse.setFinalCard(expense.getFinalCard().toString());
            }
            if (Objects.nonNull(expense.getQuantityPart())) {
                expenseResponse.setQuantityPart(expense.getQuantityPart());
            }
            expenseList.add(expenseResponse);
        }
        response.setData(expenseList);
        return response;
    }

    public static BankMovement createBankMovement(ExpenseRequest res, Account account, Expense expense) {
        BankMovement bankMovement = new BankMovement();
        bankMovement.setType("Saída");
        bankMovement.setValue(new BigDecimal(res.getValue().replace(",", ".")));
        bankMovement.setOwnerId(res.getOwnerId());
        bankMovement.setBankId(account.getBank().getId());
        bankMovement.setAccountId(account.getId());
        bankMovement.setDateMovement(FormatDate.formatDate(res.getDateBuy()));
        bankMovement.setCurrency(account.getCurrency());
        bankMovement.setExpenseId(expense.getId());
        bankMovement.setReferencePeriod(res.getDateBuy());
        bankMovement.setUserAuthId(res.getUserAuthId());
        bankMovement.setCreatedIn(new Date());
        bankMovement.setDeleted(false);

        return bankMovement;
    }

    public static BankMovement createBankMovementMoney(ExpenseRequest res, Money money, Expense expense) {
        BankMovement bankMovement = new BankMovement();
        bankMovement.setType("Saída");
        bankMovement.setValue(new BigDecimal(res.getValue().replace(",", ".")));
        bankMovement.setOwnerId(res.getOwnerId());
        bankMovement.setMoneyId(money.getId());
        bankMovement.setDateMovement(FormatDate.formatDate(res.getDateBuy()));
        bankMovement.setCurrency(money.getCurrency());
        bankMovement.setExpenseId(expense.getId());
        bankMovement.setReferencePeriod(res.getDateBuy());
        bankMovement.setUserAuthId(res.getUserAuthId());
        bankMovement.setCreatedIn(new Date());
        bankMovement.setDeleted(false);

        return bankMovement;
    }

    public static BankMovement createBankMovementFinancialEntity(ExpenseRequest res, CardFinancialEntity card, Expense expense) {
        BankMovement bankMovement = new BankMovement();
        bankMovement.setType("Saída");
        bankMovement.setValue(new BigDecimal(res.getValue().replace(",", ".")));
        bankMovement.setOwnerId(res.getOwnerId());
        bankMovement.setDateMovement(FormatDate.formatDate(res.getDateBuy()));
        bankMovement.setCurrency(card.getCurrency());
        bankMovement.setFinancialEntityId(card.getFinancialEntity().getId());
        bankMovement.setFinancialEntityCardId(card.getId());
        bankMovement.setExpenseId(expense.getId());
        bankMovement.setReferencePeriod(res.getDateBuy());
        bankMovement.setUserAuthId(res.getUserAuthId());
        bankMovement.setCreatedIn(new Date());
        bankMovement.setDeleted(false);

        return bankMovement;
    }

}
