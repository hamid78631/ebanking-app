package org.banking.ebanking_backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {

    private List<AccountOperationDTO> history;
    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOS;
}