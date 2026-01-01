package org.banking.ebanking_backend.web;

import lombok.AllArgsConstructor;
import org.banking.ebanking_backend.dtos.AccountHistoryDTO;
import org.banking.ebanking_backend.dtos.AccountOperationDTO;
import org.banking.ebanking_backend.dtos.BankAccountDTO;
import org.banking.ebanking_backend.exceptions.BankAccountExceptionNotFound;
import org.banking.ebanking_backend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    // Récupérer un compte spécifique par son ID
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
    public AccountHistoryDTO  getAccountHistory(@PathVariable String accountId, @RequestParam(name="page", defaultValue = "0") int pages ,
                                               @RequestParam(name="size", defaultValue = "5")int size ) throws BankAccountExceptionNotFound {

        return bankAccountService.getAccountHistory(accountId,pages , size);
    }

}