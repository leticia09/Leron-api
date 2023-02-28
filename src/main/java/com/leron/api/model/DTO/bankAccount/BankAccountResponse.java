package com.leron.api.model.DTO.bankAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankAccountResponse {
    private Long id;
    private String nickName;
    private String accountNumber;
    private String status;
    private Long idBank;
    private Long idUser;
    private String bankName;
    private String userName;
}
