package com.leron.api.model.DTO.bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankResponse {
    private Long id;
    private String name;
    private String status;
    private Long userAuthId;
}
