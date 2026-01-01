package org.banking.ebanking_backend.mapers;

import org.banking.ebanking_backend.dtos.AccountOperationDTO;
import org.banking.ebanking_backend.dtos.CurrentBankAccountDTO;
import org.banking.ebanking_backend.dtos.CustomerDTO;
import org.banking.ebanking_backend.dtos.SavingBankAccountDTO;
import org.banking.ebanking_backend.entities.AccountOperation;
import org.banking.ebanking_backend.entities.CurrentAccount;
import org.banking.ebanking_backend.entities.Customer;
import org.banking.ebanking_backend.entities.SaveAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMappers {

    // Convertir Entity → DTO (Customer)
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    // Convertir DTO → Entity (Customer)
    public Customer toCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    // Convertir Entity → DTO (SaveAccount)
    public SavingBankAccountDTO fromSavingBankAccount(SaveAccount saveAccount) {
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(saveAccount, savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(saveAccount.getCustomer()));
        savingBankAccountDTO.setType(savingBankAccountDTO.getClass().getSimpleName());
        return savingBankAccountDTO;
    }

    // Convertir DTO → Entity (SaveAccount)
    public SaveAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO) {
        SaveAccount saveAccount = new SaveAccount();
        BeanUtils.copyProperties(savingBankAccountDTO, saveAccount);
        saveAccount.setCustomer(toCustomer(savingBankAccountDTO.getCustomerDTO()));
        return saveAccount;
    }

    // Convertir Entity → DTO (CurrentAccount)
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentBankAccountDTO.getClass().getSimpleName());
        return currentBankAccountDTO;
    }

    // Convertir DTO → Entity (CurrentAccount)
    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
        currentAccount.setCustomer(toCustomer(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }
}