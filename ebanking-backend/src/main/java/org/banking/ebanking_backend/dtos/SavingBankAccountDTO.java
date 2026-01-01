package org.banking.ebanking_backend.dtos;

import jakarta.persistence.*;

import lombok.Data;

import lombok.Setter;
import org.banking.ebanking_backend.entities.Customer;
import org.banking.ebanking_backend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Data
@Setter
public class SavingBankAccountDTO extends BankAccountDTO{
    @Id
    private String id ;
    private double  balance;
    private Date createdAt;
    private AccountStatus status ;
    private double interestRate;
    private CustomerDTO customerDTO;
}
