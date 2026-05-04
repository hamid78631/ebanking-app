package org.banking.ebanking_backend.dtos;

import org.banking.ebanking_backend.enums.AccountStatus;

import java.util.Date;

public class CurrentBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private double overdraft;
    private CustomerDTO customerDTO;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public double getOverdraft() { return overdraft; }
    public void setOverdraft(double overdraft) { this.overdraft = overdraft; }
    public CustomerDTO getCustomerDTO() { return customerDTO; }
    public void setCustomerDTO(CustomerDTO customerDTO) { this.customerDTO = customerDTO; }
}