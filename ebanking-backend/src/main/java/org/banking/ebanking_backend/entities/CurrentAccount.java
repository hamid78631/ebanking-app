package org.banking.ebanking_backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CA")
public class CurrentAccount extends BankAccount {
    private double overdraft;

    public CurrentAccount() {}

    public double getOverdraft() { return overdraft; }
    public void setOverdraft(double overdraft) { this.overdraft = overdraft; }
}