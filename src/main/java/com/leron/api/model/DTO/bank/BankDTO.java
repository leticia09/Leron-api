package com.leron.api.model.DTO.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankDTO {
    private Long id;
    private String name;
    private String status;
}
