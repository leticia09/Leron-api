package com.leron.api.mapper.expense;

import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.entities.*;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatDate;
import com.leron.api.utils.GetStatusPayment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.leron.api.utils.GetStatusPayment.getMonthFinished;

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
        expense.setCurrency(account.getCurrency());
        expense.setHasSplitExpense(res.getHasSplitExpense());
        expense.setStatus("Quitado");
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
        expense.setHasSplitExpense(true);

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

    public static Expense requestToEntity(ExpenseRequest res, List<Card> cards, List<Money> moneyList, List<CardFinancialEntity> cardFinancialEntityList) {
        Expense expense = new Expense();
        Optional<Card> cardOptional = cards.stream().filter(ca -> ca.getFinalNumber().equals(res.getFinalCard())).findFirst();
        if (cardOptional.isPresent()) {
            expense.setAccountId(cardOptional.get().getAccount().getId());
            expense.setBankId(cardOptional.get().getAccount().getBank().getId());
            expense.setCurrency(cardOptional.get().getAccount().getCurrency());
        }

        expense.setLocal(res.getLocal());
        expense.setMacroGroup(res.getMacroGroup());
        expense.setOwnerId(res.getOwnerId());
        expense.setPaymentForm(res.getPaymentForm());
        expense.setHasFixed(res.getHasFixed());
        expense.setStatus("Quitado");

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
            Optional<Money> money = moneyList.stream().filter(mo -> mo.getId().equals(res.getMoneyId())).findFirst();
            money.ifPresent(value -> expense.setCurrency(value.getCurrency()));
        }

        if (Objects.nonNull(res.getTicketId())) {
            expense.setFinancialEntityId(res.getTicketId());
        }

        if (Objects.nonNull(res.getCardId())) {
            expense.setFinancialEntityCardId(res.getCardId());
            Optional<CardFinancialEntity> card = cardFinancialEntityList.stream().filter(mo -> mo.getId().equals(res.getTicketId())).findFirst();
            card.ifPresent(value -> expense.setCurrency(value.getCurrency()));
        }

        if (res.getPaymentForm().equalsIgnoreCase("Crédito")) {
            expense.setHasSplitExpense(true);
        }


        return expense;
    }

    public static List<ExpenseResponse> entityToResponse(List<Expense> expenses, List<Member> members, List<Card> cards, List<BankMovement> bankMovements, int month, int year, List<Account> accounts, List<Money> moneyList, List<CardFinancialEntity> cardFinancialEntityList) {

        List<ExpenseResponse> expenseList = new ArrayList<>();
        String monthValue;
        if (month < 10) {
            monthValue = "0" + month;
        } else {
            monthValue = "" + month;
        }
        members.forEach(member -> {
            for (Expense expense : expenses) {
                if (expense.getOwnerId().equals(member.getId())) {
                    ExpenseResponse expenseResponse = new ExpenseResponse();
                    expenseResponse.setId(expense.getId());
                    expenseResponse.setLocal(expense.getLocal());
                    expenseResponse.setMacroGroup(expense.getMacroGroup());
                    expenseResponse.setOwnerId(expense.getOwnerId());
                    expenseResponse.setPaymentForm(expense.getPaymentForm());
                    expenseResponse.setHasFixed(expense.getHasFixed());
                    expenseResponse.setDateBuy(expense.getDateBuy());
                    expenseResponse.setValue(expense.getValue());
                    expenseResponse.setHasSplitExpense(expense.getHasSplitExpense());

                    if (Objects.nonNull(expense.getObs())) {
                        expenseResponse.setObs(expense.getObs());
                    }

                    if (Objects.nonNull(expense.getDayPayment())) {
                        expenseResponse.setDayPayment(expense.getDayPayment());
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
                        expenseResponse.setDayPayment(expense.getDayPayment());
                    }

                    if (Objects.nonNull(expense.getFinancialEntityCardId())) {
                        Optional<CardFinancialEntity> card = cardFinancialEntityList.stream().filter(c -> c.getId().equals(expense.getFinancialEntityCardId())).findFirst();
                        card.ifPresent(value -> expenseResponse.setCurrency(value.getCurrency()));
                        card.ifPresent(value -> expenseResponse.setFinalCard(value.getFinalCard()));
                        expenseResponse.setDayPayment(expense.getDayPayment());
                    }

                    if (Objects.nonNull(expense.getAccountId())) {
                        Optional<Account> accountOptional = accounts.stream().filter(a -> a.getId().equals(expense.getAccountId())).findFirst();
                        accountOptional.ifPresent(account -> expenseResponse.setCurrency(account.getCurrency()));
                    }

                    if (expense.getHasSplitExpense()) {
                        BigDecimal c = expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32);
                        expenseResponse.setPartValue(c);
                    }

                    Optional<Card> cardOptional = cards.stream().filter(ca -> ca.getFinalNumber().equals(expense.getFinalCard())).findFirst();
                    String monthValidate = "" + month;
                    if (month < 10) {
                        monthValidate = "0" + month;
                    }
                    String period = monthValidate + "/" + year;
                    List<BankMovement> bankMovementList = bankMovements
                            .stream()
                            .filter(bm -> Objects.nonNull(bm.getExpenseId()) &&
                                    bm.getReferencePeriod().equalsIgnoreCase(period) &&
                                    bm.getExpenseId().equals(expense.getId()
                                    )).collect(Collectors.toList());

                    cardOptional.ifPresent(card -> expenseResponse.setFinalCard(expense.getFinalCard()));

                    String status = GetStatusPayment.getStatus(expense, bankMovementList, month, year);

                    LocalDate dateBuy;
                    if (Objects.nonNull(expense.getInitialDate())) {
                        dateBuy = expense.getInitialDate().toLocalDateTime().toLocalDate();
                    } else {
                        dateBuy = expense.getDateBuy().toLocalDateTime().toLocalDate();
                    }

                  if (expense.getHasSplitExpense() && (status.equalsIgnoreCase("aguardando") || status.equalsIgnoreCase("confirmado") || status.equalsIgnoreCase("pendente"))) {
                        LocalDate data = LocalDate.of(year, month, 1);
                        int part = month - dateBuy.getMonthValue() + 1;
                        expenseResponse.setPartNumber(part);
                      if(expense.getQuantityPart() == 1) {
                          expenseResponse.setPartNumber(1);
                      } else
                      if (expense.getPaymentForm().equalsIgnoreCase("crédito") &&
                                dateBuy.isBefore(data) &&
                                cardOptional.isPresent() &&
                                cardOptional.get().getClosingDate() < dateBuy.getDayOfMonth()) {

                            int monthFinished = getMonthFinished(expense, dateBuy, cardOptional.get());
                            int numberOfParts = Math.toIntExact(expense.getQuantityPart());

                            if (expense.getQuantityPart() == 1) {
                                expenseResponse.setPartNumber(1);
                            } else if (numberOfParts == 1 || month - 1 == 0 || monthFinished >= month) {
                                expenseResponse.setPartNumber(month);
                            } else {
                                expenseResponse.setPartNumber(month - 1);
                            }
                        }


                        BigDecimal c = expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32);
                        expenseResponse.setPartValue(c);
                    }


                    if (status.equalsIgnoreCase("Não Iniciada")) {
                        expenseResponse.setStatus("Não Iniciada");
                    }

                    if (status.equalsIgnoreCase("Aguardando")) {
                        expenseResponse.setStatus("Aguardando");
                    }

                    if (status.equalsIgnoreCase("Pendente")) {
                        expenseResponse.setStatus("Pendente");
                    }
                    if (status.equalsIgnoreCase("Confirmado")) {
                        expenseResponse.setStatus("Confirmado");
                        List<BankMovement> bankMovement1 = bankMovementList.stream()
                                .filter(bm -> Objects.equals(bm.getExpenseId(), expense.getId()) &&
                                        bm.getReferencePeriod().equalsIgnoreCase(monthValue + "/" + year) &&
                                        bm.getType().equalsIgnoreCase("Saída")
                                ).collect(Collectors.toList());


                        BigDecimal value = bankMovement1.stream().map(BankMovement::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (expense.getPaymentForm().equalsIgnoreCase("vale")) {
                            expenseResponse.setValuePaid(expense.getValue());
                        } else {
                            expenseResponse.setValuePaid(value);
                        }

                    }

                    expenseList.add(expenseResponse);


                }
            }
        });

        return expenseList.stream().filter(expense -> Objects.nonNull(expense.getStatus()) &&
                !expense.getStatus().equalsIgnoreCase("Não Iniciada") &&
                !expense.getStatus().equalsIgnoreCase("Fechada")).collect(Collectors.toList());
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
                expenseResponse.setFinalCard(expense.getFinalCard());
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
        bankMovement.setCurrency(account.getCurrency());
        bankMovement.setExpenseId(expense.getId());
        if (Objects.nonNull(res.getDateBuy())) {
            bankMovement.setDateMovement(FormatDate.formatDate(res.getDateBuy()));
            LocalDate initialDate = expense.getDateBuy().toLocalDateTime().toLocalDate();
            String valueReference = "";
            if (initialDate.getMonthValue() < 10) {
                valueReference = "0" + initialDate.getMonthValue();
            } else {
                valueReference = initialDate.getMonthValue() + "";
            }
            bankMovement.setReferencePeriod(valueReference + "/" + initialDate.getYear());
        }
        if (Objects.nonNull(res.getInitialDate())) {
            bankMovement.setDateMovement(FormatDate.formatDate(res.getInitialDate()));
            LocalDate initialDate = expense.getInitialDate().toLocalDateTime().toLocalDate();
            String valueReference = "";
            if (initialDate.getMonthValue() < 10) {
                valueReference = "0" + initialDate.getMonthValue();
            } else {
                valueReference = initialDate.getMonthValue() + "";
            }
            bankMovement.setReferencePeriod(valueReference + "/" + initialDate.getYear());
        }
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
        if (Objects.nonNull(res.getDateBuy())) {
            bankMovement.setDateMovement(FormatDate.formatDate(res.getDateBuy()));
            LocalDate initialDate = expense.getDateBuy().toLocalDateTime().toLocalDate();
            bankMovement.setReferencePeriod(initialDate.getMonthValue() + "/" + initialDate.getYear());
        }
        if (Objects.nonNull(res.getInitialDate())) {
            bankMovement.setDateMovement(FormatDate.formatDate(res.getInitialDate()));
            LocalDate initialDate = expense.getInitialDate().toLocalDateTime().toLocalDate();
            bankMovement.setReferencePeriod(initialDate.getMonthValue() + "/" + initialDate.getYear());
        }
        bankMovement.setCurrency(money.getCurrency());
        bankMovement.setExpenseId(expense.getId());
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
        bankMovement.setCurrency(card.getCurrency());
        bankMovement.setFinancialEntityId(card.getFinancialEntity().getId());
        bankMovement.setFinancialEntityCardId(card.getId());
        bankMovement.setExpenseId(expense.getId());
        if (Objects.nonNull(res.getDateBuy())) {
            bankMovement.setDateMovement(FormatDate.formatDate(res.getDateBuy()));
            LocalDate initialDate = expense.getDateBuy().toLocalDateTime().toLocalDate();
            String month = (initialDate.getMonthValue() < 10) ? "0" + initialDate.getMonthValue() : initialDate.getMonthValue() + "";

            bankMovement.setReferencePeriod(month + "/" + initialDate.getYear());
        }
        if (Objects.nonNull(res.getInitialDate())) {
            bankMovement.setDateMovement(FormatDate.formatDate(res.getInitialDate()));
            LocalDate initialDate = expense.getInitialDate().toLocalDateTime().toLocalDate();
            bankMovement.setReferencePeriod(initialDate.getMonthValue() + "/" + initialDate.getYear());
        }
        bankMovement.setUserAuthId(res.getUserAuthId());
        bankMovement.setCreatedIn(new Date());
        bankMovement.setDeleted(false);

        return bankMovement;
    }

}
