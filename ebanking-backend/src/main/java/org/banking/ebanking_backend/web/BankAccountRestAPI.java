package org.banking.ebanking_backend.web;

import org.banking.ebanking_backend.dtos.*;
import org.banking.ebanking_backend.exceptions.BalanceNotSufficentException;
import org.banking.ebanking_backend.exceptions.BankAccountExceptionNotFound;
import org.banking.ebanking_backend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private final BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountExceptionNotFound {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping({"/accounts", "/accounts/"})
    public List<BankAccountDTO> getBankAccounts() {
        return bankAccountService.bankAccountsList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int pages,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountExceptionNotFound {
        return bankAccountService.getAccountHistory(accountId, pages, size);
    }

    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountExceptionNotFound, BalanceNotSufficentException {
        bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountExceptionNotFound {
        bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferDTO transferDTO) throws BankAccountExceptionNotFound, BalanceNotSufficentException {
        bankAccountService.transfert(transferDTO.getAccountSource(), transferDTO.getAccountDestination(), transferDTO.getAmount());
    }

    record CreateCurrentRequest(Long customerId, double initialBalance, double overdraft) {}
    record CreateSavingRequest(Long customerId, double initialBalance, double interestRate) {}

    @PostMapping("/accounts/current")
    public BankAccountDTO createCurrentAccount(@RequestBody CreateCurrentRequest req) throws Exception {
        return bankAccountService.saveCurrentBankAccount(req.initialBalance(), req.overdraft(), req.customerId());
    }

    @PostMapping("/accounts/saving")
    public BankAccountDTO createSavingAccount(@RequestBody CreateSavingRequest req) throws Exception {
        return bankAccountService.saveSavingBankAccount(req.initialBalance(), req.interestRate(), req.customerId());
    }
}