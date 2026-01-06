package org.banking.ebanking_backend;

import org.banking.ebanking_backend.dtos.BankAccountDTO;
import org.banking.ebanking_backend.dtos.CurrentBankAccountDTO;
import org.banking.ebanking_backend.dtos.CustomerDTO;
import org.banking.ebanking_backend.dtos.SavingBankAccountDTO;
import org.banking.ebanking_backend.entities.*;
import org.banking.ebanking_backend.enums.AccountStatus;
import org.banking.ebanking_backend.enums.OperationType;
import org.banking.ebanking_backend.exceptions.CustomerNotFoundException;
import org.banking.ebanking_backend.repositories.AccountOperationRepository;
import org.banking.ebanking_backend.repositories.BankAccountRepository;
import org.banking.ebanking_backend.repositories.CustomerRepository;
import org.banking.ebanking_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

public class EbankingBackendApplication {

	public static void main(String[] args) {

        SpringApplication.run(EbankingBackendApplication.class, args);
	}
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {

        return args ->{

            Stream.of("Hassan", "Hamid", "Imane").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name +"@gmail.com");

               bankAccountService.saveCustomer(customer);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*9000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }


            });
            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountsList();
            for(BankAccountDTO bankAccount : bankAccounts){
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if(bankAccount instanceof SavingBankAccountDTO){
                        accountId= ((SavingBankAccountDTO) bankAccount).getId();
                    }else{
                        accountId= ((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId, 1000 + Math.random(), "Credit");
                    bankAccountService.debit(accountId,1000 + Math.random(), "debit");
                }
            }
        };
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository , BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){

        return args -> {
            Stream.of("Hassan" , "Yassine" , "Aicha").forEach(name -> {
                Customer customer  = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setCustomer(customer);
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setOverdraft(9000);
                bankAccountRepository.save(currentAccount);
            });

            customerRepository.findAll().forEach(customer -> {
                SaveAccount saveAccount = new SaveAccount();
                saveAccount.setBalance(Math.random()*9000);
                saveAccount.setId(UUID.randomUUID().toString());
                saveAccount.setCreatedAt(new Date());
                saveAccount.setCustomer(customer);
                saveAccount.setInterestRate(5.5 );
                saveAccount.setStatus(AccountStatus.CREATED);
                bankAccountRepository.save(saveAccount);
            });

            bankAccountRepository.findAll().forEach(acc -> {
                for (int i = 0; i < 5; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setAmount(Math.random()*9000);
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.CREDIT : OperationType.DEBIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }
}
