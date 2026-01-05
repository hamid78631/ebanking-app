package org.banking.ebanking_backend.services;

import org.banking.ebanking_backend.dtos.*;
import org.banking.ebanking_backend.entities.BankAccount;
import org.banking.ebanking_backend.entities.CurrentAccount;
import org.banking.ebanking_backend.entities.Customer;
import org.banking.ebanking_backend.entities.SaveAccount;
import org.banking.ebanking_backend.exceptions.BalanceNotSufficentException;
import org.banking.ebanking_backend.exceptions.BankAccountExceptionNotFound;
import org.banking.ebanking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

     CustomerDTO saveCustomer(CustomerDTO customerDTO);
     CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance , double overDraft, Long customerId) throws CustomerNotFoundException;
     SavingBankAccountDTO saveSavingBankAccount(double initialBalance , double interestRate , Long customerId) throws CustomerNotFoundException;

    BankAccount saveCurrentBankAccount(double initialBalance, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
     BankAccountDTO getBankAccount(String accountId ) throws BankAccountExceptionNotFound;
     void debit( String accountId , double amount , String description ) throws BankAccountExceptionNotFound, BalanceNotSufficentException;
     void credit( String accountId , double amount , String description ) throws BankAccountExceptionNotFound;
     void transfert(String accountIdSource , String accountIdDestination, double amount) throws BalanceNotSufficentException, BankAccountExceptionNotFound;

    List<BankAccountDTO> bankAccountsList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long CustomerId);


    List<AccountOperationDTO> accountHistory(String accountId);

    List<CustomerDTO> searchCustomers(String keyword);
    AccountHistoryDTO getAccountHistory(String accountId, int pages, int size) throws BankAccountExceptionNotFound;
}
