package com.leron.api.mapper.bankMovement;

import com.leron.api.model.DTO.BankMovement.BankMovementResponse;
import com.leron.api.model.DTO.BankMovement.PaymentRequest;
import com.leron.api.model.DTO.BankMovement.ReceiveRequest;
import com.leron.api.model.DTO.BankMovement.TransferBankRequest;
import com.leron.api.model.DTO.expense.ExpensePeriodResponse;
import com.leron.api.model.entities.*;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatDate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.*;

@Component
public class BankMovementMapper {

    public static DataListResponse<BankMovementResponse> entitiesToResponse(List<BankMovement> request) {
        DataListResponse<BankMovementResponse> response = new DataListResponse<>();
        List<BankMovementResponse> responses = new ArrayList<>();

        request.forEach(res -> {
            BankMovementResponse bankMovementResponse = new BankMovementResponse();
            bankMovementResponse.setId(res.getId());
            bankMovementResponse.setBankId(res.getBankId());
            bankMovementResponse.setDateMovement(res.getDateMovement());
            bankMovementResponse.setObs(res.getObs());
            bankMovementResponse.setExpenseId(res.getExpenseId());
            bankMovementResponse.setReferencePeriod(res.getReferencePeriod());
            bankMovementResponse.setValue(res.getValue());
            bankMovementResponse.setType(res.getType());
            bankMovementResponse.setAccountId(res.getAccountId());
            bankMovementResponse.setOwnerId(res.getOwnerId());
            bankMovementResponse.setEntranceId(res.getEntranceId());
            bankMovementResponse.setCurrency(res.getCurrency());
            responses.add(bankMovementResponse);
        });
        response.setData(responses);
        return response;
    }

    public static BankMovement payToBankMovement(PaymentRequest requests, List<Expense> expenses, Long userAuth, List<Account> accounts, List<CardFinancialEntity> cardFinancialEntities, Money moneyList) {

        Optional<Expense> expense = expenses.stream()
                .filter(expense1 -> expense1.getId().equals(requests.getExpenseId()))
                .findFirst();

        BankMovement bankMovement = new BankMovement();
        bankMovement.setDateMovement(FormatDate.formatDate(requests.getPaymentDate()));
        bankMovement.setOwnerId(requests.getOwnerId());
        bankMovement.setType("Saída");
        bankMovement.setReferencePeriod(requests.getReferencePeriod());
        bankMovement.setDeleted(false);
        bankMovement.setCreatedIn(new Date());
        bankMovement.setUserAuthId(userAuth);
        String salaryText = requests.getValue();

        salaryText = salaryText.replaceAll("[^\\d.,]", "");
        salaryText = salaryText.replaceAll("\\.", "");
        salaryText = salaryText.replace(",", ".");
        BigDecimal salary = new BigDecimal(salaryText);

        bankMovement.setValue(salary);
        bankMovement.setObs(requests.getObs());

        if (expense.isPresent()) {
            Optional<Account> account = accounts.stream().filter(ac -> Objects.equals(ac.getId(), expense.get().getAccountId())).findFirst();
            Optional<CardFinancialEntity> cardFinancial = cardFinancialEntities.stream().filter(cf -> cf.getId().equals(expense.get().getFinancialEntityCardId())).findFirst();

            bankMovement.setExpenseId(expense.get().getId());
            bankMovement.setAccountId(expense.get().getAccountId());
            bankMovement.setBankId(expense.get().getBankId());

            account.ifPresent(value -> bankMovement.setCurrency(value.getCurrency()));

            if (cardFinancial.isPresent()) {
                bankMovement.setCurrency(cardFinancial.get().getCurrency());
                bankMovement.setFinancialEntityCardId(cardFinancial.get().getId());
                bankMovement.setFinancialEntityId(cardFinancial.get().getFinancialEntity().getId());
            }

            if (Objects.nonNull(moneyList)) {
                bankMovement.setMoneyId(moneyList.getId());
                bankMovement.setCurrency(moneyList.getCurrency());
            }
        }

        return bankMovement;
    }

    public static BankMovement paymentCreditToBankMovement(PaymentRequest request, Expense expense, Long userAuth, Account account, Card card, ExpensePeriodResponse expensePeriodResponse) {

        BankMovement bankMovement = new BankMovement();
        bankMovement.setDateMovement(FormatDate.formatDate(request.getPaymentDate()));
        bankMovement.setOwnerId(request.getOwnerId());
        bankMovement.setType("Saída");
        bankMovement.setReferencePeriod(request.getReferencePeriod());
        bankMovement.setDeleted(false);
        bankMovement.setCreatedIn(new Date());
        bankMovement.setUserAuthId(userAuth);
        String value = expensePeriodResponse.getValue();
        value = value.replace(",", ".");
        value = value.replaceAll("[^\\d.]", "");
        bankMovement.setValue(new BigDecimal(value));
        bankMovement.setObs("Pagamento Fatura Cartão: " + card.getName() + "/" + card.getFinalNumber());

        bankMovement.setExpenseId(expense.getId());
        bankMovement.setAccountId(expense.getAccountId());
        bankMovement.setBankId(expense.getBankId());
        bankMovement.setCurrency(account.getCurrency());

        return bankMovement;
    }


    public static BankMovement receiveToBankMovement(ReceiveRequest requests, List<Entrance> entrances, Long userAuth, List<Account> accounts, List<CardFinancialEntity> cardFinancialEntities, Money moneyList) {

        Optional<Entrance> entrance = entrances.stream()
                .filter(entrance1 -> entrance1.getId().toString().equals(requests.getEntrance()))
                .findFirst();

        BankMovement bankMovement = new BankMovement();
        bankMovement.setDateMovement(FormatDate.formatDate(requests.getReceiveDate()));
        bankMovement.setOwnerId(requests.getOwnerId());
        bankMovement.setType("Entrada");
        bankMovement.setReferencePeriod(requests.getReferencePeriod());
        bankMovement.setDeleted(false);
        bankMovement.setCreatedIn(new Date());
        bankMovement.setUserAuthId(userAuth);
        String salaryText = requests.getSalary();

        salaryText = salaryText.replaceAll("[^\\d.,]", "");
        salaryText = salaryText.replaceAll("\\.", "");
        salaryText = salaryText.replace(",", ".");
        BigDecimal salary = new BigDecimal(salaryText);

        bankMovement.setValue(salary);
        bankMovement.setObs(requests.getObs());

        if (entrance.isPresent()) {
            Optional<Account> account = accounts.stream().filter(ac -> Objects.equals(ac.getId(), entrance.get().getAccountId())).findFirst();
            Optional<CardFinancialEntity> cardFinancial = cardFinancialEntities.stream().filter(cf -> cf.getId().equals(entrance.get().getFinancialEntityCardId())).findFirst();

            bankMovement.setEntranceId(entrance.get().getId());
            bankMovement.setAccountId(entrance.get().getAccountId());
            bankMovement.setBankId(entrance.get().getBankId());

            account.ifPresent(value -> bankMovement.setCurrency(value.getCurrency()));

            if (cardFinancial.isPresent()) {
                bankMovement.setCurrency(cardFinancial.get().getCurrency());
                bankMovement.setFinancialEntityCardId(cardFinancial.get().getId());
                bankMovement.setFinancialEntityId(cardFinancial.get().getFinancialEntity().getId());
            }

            if (Objects.nonNull(moneyList)) {
                bankMovement.setMoneyId(moneyList.getId());
                bankMovement.setCurrency(moneyList.getCurrency());
            }
        }

        return bankMovement;
    }

    public static Account paymentToAccount(PaymentRequest request, List<Account> accounts, List<Expense> expenses) {
        Optional<Expense> expense = expenses.stream()
                .filter(expense1 -> expense1.getId().equals(request.getExpenseId()))
                .findFirst();
        if (expense.isPresent()) {
            Optional<Account> account = accounts.stream().filter(account1 -> account1.getId().toString().equalsIgnoreCase(expense.get().getAccountId().toString())).findFirst();

            if (account.isPresent()) {
                String salaryText = request.getValue();
                salaryText = salaryText.replaceAll("[^\\d.,]", "");
                salaryText = salaryText.replaceAll("\\.", "");
                salaryText = salaryText.replace(",", ".");
                BigDecimal salary = new BigDecimal(salaryText);
                BigDecimal oldValue = account.get().getValue();
                BigDecimal value = oldValue.subtract(salary);
                account.get().setValue(value);
                return account.get();
            }
        }

        return null;
    }

    public static Account receiveToAccount(ReceiveRequest request, List<Account> accounts, List<Entrance> entrances) {
        Optional<Entrance> entrance = entrances.stream()
                .filter(entrance1 -> entrance1.getId().toString().equals(request.getEntrance()))
                .findFirst();
        if (entrance.isPresent()) {
            Optional<Account> account = accounts.stream().filter(account1 -> account1.getId().toString().equalsIgnoreCase(entrance.get().getAccountId().toString())).findFirst();

            if (account.isPresent()) {
                String salaryText = request.getSalary();
                salaryText = salaryText.replaceAll("[^\\d.,]", "");
                salaryText = salaryText.replaceAll("\\.", "");
                salaryText = salaryText.replace(",", ".");
                BigDecimal salary = new BigDecimal(salaryText);
                BigDecimal oldValue = account.get().getValue();
                BigDecimal value = salary.add(oldValue);
                account.get().setValue(value);
                return account.get();
            }
        }

        return null;
    }

    public static Money receiveToMoney(ReceiveRequest requests, List<Entrance> entrances, Long userAuthId, List<Money> moneyList) {
        Optional<Entrance> entrance = entrances.stream()
                .filter(entrance1 -> entrance1.getId().toString().equals(requests.getEntrance()))
                .findFirst();
        if (entrance.isPresent()) {

            Optional<Money> money = moneyList.stream().filter(mo -> mo.getId().equals(entrance.get().getMoneyId())).findFirst();

            if (money.isPresent()) {
                String salaryText = requests.getSalary();
                salaryText = salaryText.replaceAll("[^\\d.,]", "");
                salaryText = salaryText.replaceAll("\\.", "");
                salaryText = salaryText.replace(",", ".");
                BigDecimal salary = new BigDecimal(salaryText);
                BigDecimal oldValue = money.get().getValue();
                BigDecimal value = salary.add(oldValue);
                money.get().setValue(value);
                return money.get();
            }
        }

        return null;
    }

    public static Money payToMoney(PaymentRequest requests, List<Expense> expenses, Long userAuthId, List<Money> moneyList) {
        Optional<Expense> expense = expenses.stream()
                .filter(expense1 -> expense1.getId().equals(requests.getExpenseId()))
                .findFirst();
        if (expense.isPresent()) {

            Optional<Money> money = moneyList.stream().filter(mo -> mo.getId().equals(expense.get().getMoneyId())).findFirst();
            if (money.isPresent()) {
                String salaryText = requests.getValue();
                salaryText = salaryText.replaceAll("[^\\d.,]", "");
                salaryText = salaryText.replaceAll("\\.", "");
                salaryText = salaryText.replace(",", ".");
                BigDecimal salary = new BigDecimal(salaryText);
                BigDecimal oldValue = money.get().getValue();
                BigDecimal value = salary.subtract(oldValue);
                money.get().setValue(value);
                return money.get();
            }
        }

        return null;
    }

    public static CardFinancialEntity receiveToFinancial(ReceiveRequest requests, List<CardFinancialEntity> cardFinancialEntities, List<Entrance> entrances) {
        List<CardFinancialEntity> response = new ArrayList<>();


        Optional<Entrance> entrance = entrances.stream()
                .filter(entrance1 -> entrance1.getId().toString().equals(requests.getEntrance()))
                .findFirst();
        if (entrance.isPresent()) {

            Optional<CardFinancialEntity> cardFinancial = cardFinancialEntities.stream().filter(account1 -> account1.getId().equals(entrance.get().getFinancialEntityCardId())).findFirst();

            if (cardFinancial.isPresent()) {
                String salaryText = requests.getSalary();
                salaryText = salaryText.replaceAll("[^\\d.,]", "");
                salaryText = salaryText.replaceAll("\\.", "");
                salaryText = salaryText.replace(",", ".");
                BigDecimal salary = new BigDecimal(salaryText);
                BigDecimal oldValue = cardFinancial.get().getBalance();
                BigDecimal value = salary.add(oldValue);
                cardFinancial.get().setBalance(value);
                return cardFinancial.get();
            }
        }
        return null;
    }

    public static List<Account> transferToAccount(TransferBankRequest request, List<Account> accounts) {
        List<Account> response = new ArrayList<>();

        Optional<Account> accountReceive = accounts.stream().filter(ac -> ac.getId().equals(request.getAccountDestinyId())).findFirst();
        Optional<Account> accountRemove = accounts.stream().filter(ac -> ac.getId().equals(request.getAccountOriginId())).findFirst();

        if (accountReceive.isPresent() && accountRemove.isPresent()) {
            String valueRequest = request.getValue();
            valueRequest = valueRequest.replaceAll("[^\\d.,]", "");
            valueRequest = valueRequest.replaceAll("\\.", "");
            valueRequest = valueRequest.replace(",", ".");
            BigDecimal requestValueReceive = new BigDecimal(valueRequest);
            BigDecimal requestValueRemove = new BigDecimal(valueRequest);

            BigDecimal oldValue = accountReceive.get().getValue();
            BigDecimal value = requestValueReceive.add(oldValue);
            accountReceive.get().setValue(value);

            BigDecimal oldValueRemove = accountRemove.get().getValue();
            BigDecimal valueRemoved = oldValueRemove.subtract(requestValueRemove);
            accountRemove.get().setValue(valueRemoved);

            response.add(accountReceive.get());
            response.add(accountRemove.get());
        }

        return response;
    }

    public static List<BankMovement> transferToBankMovement(TransferBankRequest request, Long userAuthId, List<Account> accounts) {
        List<BankMovement> response = new ArrayList<>();

        Optional<Account> accountReceive = accounts.stream().filter(ac -> ac.getId().equals(request.getAccountDestinyId())).findFirst();
        Optional<Account> accountRemove = accounts.stream().filter(ac -> ac.getId().equals(request.getAccountOriginId())).findFirst();

        BankMovement bankMovementPositive = new BankMovement();
        bankMovementPositive.setUserAuthId(userAuthId);
        bankMovementPositive.setType("Trasferência Positiva");
        bankMovementPositive.setValue(new BigDecimal(request.getValue().replace(",", ".")));
        bankMovementPositive.setBankId(request.getBankDestinyId());
        bankMovementPositive.setAccountId(request.getAccountDestinyId());
        bankMovementPositive.setDateMovement(FormatDate.formatDate(request.getDateTransfer()));
        bankMovementPositive.setOwnerId(request.getOwnerDestinyId());
        bankMovementPositive.setObs(request.getObs());
        bankMovementPositive.setDeleted(false);
        bankMovementPositive.setCreatedIn(new Date());

        BankMovement bankMovementNegative = new BankMovement();
        bankMovementNegative.setUserAuthId(userAuthId);
        bankMovementNegative.setType("Transferência Negativa");
        bankMovementNegative.setValue(new BigDecimal(request.getValue().replace(",", ".")));
        bankMovementNegative.setDateMovement(FormatDate.formatDate(request.getDateTransfer()));
        bankMovementNegative.setBankId(request.getBankOriginId());
        bankMovementNegative.setAccountId(request.getAccountOriginId());
        bankMovementNegative.setOwnerId(request.getOwnerOriginId());
        bankMovementNegative.setObs(request.getObs());


        bankMovementNegative.setDeleted(false);
        bankMovementNegative.setCreatedIn(new Date());

        if (accountReceive.isPresent() && accountRemove.isPresent()) {
            bankMovementNegative.setCurrency(accountRemove.get().getCurrency());
            bankMovementPositive.setCurrency(accountReceive.get().getCurrency());
            response.add(bankMovementPositive);
            response.add(bankMovementNegative);
        }

        return response;
    }

    public static List<Account> transferToAccountNotDestiny(TransferBankRequest request, List<Account> accounts) {
        List<Account> response = new ArrayList<>();

        Optional<Account> accountRemove = accounts.stream().filter(ac -> ac.getId().equals(request.getAccountOriginId())).findFirst();

        if (accountRemove.isPresent()) {
            String valueRequest = request.getValue();
            valueRequest = valueRequest.replaceAll("[^\\d.,]", "");
            valueRequest = valueRequest.replaceAll("\\.", "");
            valueRequest = valueRequest.replace(",", ".");
            BigDecimal requestValueRemove = new BigDecimal(valueRequest);

            BigDecimal oldValueRemove = accountRemove.get().getValue();
            BigDecimal valueRemoved = oldValueRemove.subtract(requestValueRemove);
            accountRemove.get().setValue(valueRemoved);

            response.add(accountRemove.get());
        }

        return response;
    }

    public static BankMovement transferToBankMovementNotDestiny(TransferBankRequest request, Long userAuthId, List<Account> accounts) {
        Optional<Account> accountRemove = accounts.stream().filter(ac -> ac.getId().equals(request.getAccountOriginId())).findFirst();

        BankMovement bankMovementNegative = new BankMovement();
        bankMovementNegative.setUserAuthId(userAuthId);
        bankMovementNegative.setType("Transferência Negativa");
        bankMovementNegative.setValue(new BigDecimal(request.getValue().replace(",", ".")));
        bankMovementNegative.setDateMovement(FormatDate.formatDate(request.getDateTransfer()));
        bankMovementNegative.setBankId(request.getBankOriginId());
        bankMovementNegative.setAccountId(request.getAccountOriginId());
        bankMovementNegative.setOwnerId(request.getOwnerOriginId());
        bankMovementNegative.setObs(request.getObs());
        accountRemove.ifPresent(account -> bankMovementNegative.setCurrency(account.getCurrency()));
        bankMovementNegative.setDeleted(false);
        bankMovementNegative.setCreatedIn(new Date());

        return bankMovementNegative;
    }

    public static BankMovement receiveBankMovement(ReceiveRequest request, Long userAuthId, String currency, Account account) {
        BankMovement response = new BankMovement();
        response.setType("Trasferência Positiva");
        if (!Objects.equals(request.getValue(), "") && Objects.nonNull(request.getValue())) {
            response.setValue(new BigDecimal(request.getValue().replace(",", ".")));
        }
        if (!Objects.equals(request.getSalary(), "") && Objects.nonNull(request.getSalary())) {
            String value = request.getSalary().replace(currency, "").replace(",", ".").trim();

            response.setValue(new BigDecimal(value));
        }
        response.setOwnerId(request.getOwnerId());
        response.setBankId(account.getBank().getId());
        response.setAccountId(account.getId());
        response.setDateMovement(FormatDate.formatDate(request.getReceiveDate()));
        response.setObs(request.getObs());
        if (Objects.nonNull(request.getEntrance()) && !request.getEntrance().isEmpty()) {
            response.setEntranceId(Long.valueOf(request.getEntrance()));
        }
        if (Objects.nonNull(request.getReferencePeriod())) {
            response.setReferencePeriod(request.getReferencePeriod());
        }
        response.setCurrency(currency);
        response.setUserAuthId(userAuthId);
        response.setCreatedIn(new Date());
        response.setDeleted(false);
        return response;
    }
}
