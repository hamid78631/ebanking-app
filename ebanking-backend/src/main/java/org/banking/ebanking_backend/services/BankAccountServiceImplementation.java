package org.banking.ebanking_backend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.banking.ebanking_backend.dtos.*;
import org.banking.ebanking_backend.entities.*;
import org.banking.ebanking_backend.enums.AccountStatus;
import org.banking.ebanking_backend.enums.OperationType;
import org.banking.ebanking_backend.exceptions.BalanceNotSufficentException;
import org.banking.ebanking_backend.exceptions.BankAccountExceptionNotFound;
import org.banking.ebanking_backend.exceptions.CustomerNotFoundException;
import org.banking.ebanking_backend.mapers.BankAccountMappers;
import org.banking.ebanking_backend.repositories.AccountOperationRepository;
import org.banking.ebanking_backend.repositories.BankAccountRepository;
import org.banking.ebanking_backend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; // <-- CORRECTION : Import correct
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImplementation implements BankAccountService {

    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMappers  dtoMapper ;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving customer");
        Customer customer = dtoMapper.toCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCustomer(customer);
        currentAccount.setOverdraft(overDraft);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        SaveAccount saveAccount = new SaveAccount();
        saveAccount.setId(UUID.randomUUID().toString());
        saveAccount.setCreatedAt(new Date());
        saveAccount.setBalance(initialBalance);
        saveAccount.setStatus(AccountStatus.CREATED);
        saveAccount.setCustomer(customer);
        saveAccount.setInterestRate(interestRate);
        SaveAccount savedBankAccount = bankAccountRepository.save(saveAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    @Override
    public BankAccount saveCurrentBankAccount(double initialBalance, Long customerId) throws CustomerNotFoundException {
        return null;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(dtoMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountExceptionNotFound {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountExceptionNotFound("Bank account not found"));

        if(bankAccount instanceof SaveAccount) {
            return dtoMapper.fromSavingBankAccount((SaveAccount) bankAccount);
        } else {
            return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountExceptionNotFound, BalanceNotSufficentException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountExceptionNotFound("Bank account not found"));
        if(bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficentException("Balance not sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountExceptionNotFound {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountExceptionNotFound("Bank account not found"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficentException, BankAccountExceptionNotFound {
        debit(accountIdSource, amount , "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountsList(){
        List<BankAccount> bankaccounts = bankAccountRepository.findAll();
        return bankaccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SaveAccount) {
                return dtoMapper.fromSavingBankAccount((SaveAccount) bankAccount);
            } else {
                return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            }
        }).collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer");
        Customer customer = dtoMapper.toCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long CustomerId){
        customerRepository.deleteById(CustomerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountExceptionNotFound {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountExceptionNotFound("Account not found"));

        // CORRECTION : Utilisation de l'import correct et suppression du cast inutile
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, pageable);

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream()
                .map(op -> dtoMapper.fromAccountOperation(op))
                .collect(Collectors.toList());

        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }
}