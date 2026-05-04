package org.banking.ebanking_backend.entities;

import jakarta.persistence.*;
import org.banking.ebanking_backend.enums.AccountStatus;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
public abstract class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation> operations;

    public BankAccount() {}

    public BankAccount(String id, double balance, Date createdAt, AccountStatus status, Customer customer, List<AccountOperation> operations) {
        this.id = id;
        this.balance = balance;
        this.createdAt = createdAt;
        this.status = status;
        this.customer = customer;
        this.operations = operations;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public List<AccountOperation> getOperations() { return operations; }
    public void setOperations(List<AccountOperation> operations) { this.operations = operations; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount b)) return false;
        return Objects.equals(id, b.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}