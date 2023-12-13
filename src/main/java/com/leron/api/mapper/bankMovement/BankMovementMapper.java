package com.leron.api.mapper.bankMovement;

import com.leron.api.model.DTO.BankMovement.ReceiveRequest;
import com.leron.api.model.entities.Account;
import com.leron.api.model.entities.BankMovement;
import com.leron.api.model.entities.Entrance;
import com.leron.api.utils.FormatDate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class BankMovementMapper {

    public static List<BankMovement> receiveToBankMovement(List<ReceiveRequest> requests, List<Entrance> entrances, Long userAuth) {
        List<BankMovement> response = new ArrayList<>();
        requests.forEach(request -> {
            Optional<Entrance> entrance = entrances.stream()
                    .filter(entrance1 -> entrance1.getId().toString().equals(request.getEntrance()))
                    .findFirst();
            BankMovement bankMovement = new BankMovement();
            bankMovement.setDateMovement(FormatDate.formatDate(request.getReceiveDate()));
            bankMovement.setOwnerId(request.getOwnerId());
            bankMovement.setType("Entrada");
            bankMovement.setReferencePeriod(request.getReferencePeriod());
            bankMovement.setDeleted(false);
            bankMovement.setCreatedIn(new Date());
            bankMovement.setUserAuthId(userAuth);
            String salaryText = request.getSalary();

            salaryText = salaryText.replaceAll("[^\\d.,]", "");
            salaryText = salaryText.replaceAll("\\.", "");
            salaryText = salaryText.replace(",", ".");
            BigDecimal salary = new BigDecimal(salaryText);

            bankMovement.setValue(salary);
            bankMovement.setObs(request.getObs());

            if (entrance.isPresent()) {
                bankMovement.setEntranceId(entrance.get().getId());
                bankMovement.setAccountId(entrance.get().getAccountId());
                bankMovement.setBankId(entrance.get().getBankId());
                response.add(bankMovement);
            }
        });

        return response;
    }

    public static List<Account> receiveToAccount(List<ReceiveRequest> requests, List<Account> accounts, List<Entrance> entrances) {
        List<Account> response = new ArrayList<>();

        requests.forEach(receiveRequest -> {
            Optional<Entrance> entrance = entrances.stream()
                    .filter(entrance1 -> entrance1.getId().toString().equals(receiveRequest.getEntrance()))
                    .findFirst();
            if(entrance.isPresent()) {
              Optional<Account> account = accounts.stream().filter(account1 -> account1.getId().toString().equalsIgnoreCase(entrance.get().getAccountId().toString())).findFirst();

              if(account.isPresent()) {
                  String salaryText = receiveRequest.getSalary();
                  salaryText = salaryText.replaceAll("[^\\d.,]", "");
                  salaryText = salaryText.replaceAll("\\.", "");
                  salaryText = salaryText.replace(",", ".");
                  BigDecimal salary = new BigDecimal(salaryText);
                  BigDecimal oldValue = account.get().getValue();
                  BigDecimal value = salary.add(oldValue);
                  account.get().setValue(value);
                  response.add(account.get());
              }
            }
        });


        return response;
    }

//    private static List<Entrance> receiveToEntrance (List<ReceiveRequest> requests, List<Entrance> entrances) {
//        List<Entrance> response = new ArrayList<>();
//
//        requests.forEach(request -> {
//            Optional<Entrance> entranceOptional = entrances.stream().filter(entrance -> entrance.getId().toString().equals(request.getEntrance())).findFirst();
//            if(entranceOptional.isPresent()) {
//                entranceOptional.get().setStatus("Confirmado");
//            }
//        });
//
//        return response;
//    }
}
