package org.banking.ebanking_backend;

import org.banking.ebanking_backend.entities.AccountOperation;
import org.banking.ebanking_backend.entities.CurrentAccount;
import org.banking.ebanking_backend.entities.Customer;
import org.banking.ebanking_backend.entities.SaveAccount;
import org.banking.ebanking_backend.enums.AccountStatus;
import org.banking.ebanking_backend.enums.OperationType;
import org.banking.ebanking_backend.repositories.AccountOperationRepository;
import org.banking.ebanking_backend.repositories.BankAccountRepository;
import org.banking.ebanking_backend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {

        SpringApplication.run(EbankingBackendApplication.class, args);
	}
    @Bean
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
