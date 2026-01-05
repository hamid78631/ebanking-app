package org.banking.ebanking_backend.dtos;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.banking.ebanking_backend.enums.AccountStatus;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class CurrentBankAccountDTO extends BankAccountDTO{
    @Id
    private String id ;
    private double  balance;
    private Date createdAt;
    private AccountStatus status ;
    private double overdraft;
    private CustomerDTO customerDTO;

}
