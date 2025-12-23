package org.banking.ebanking_backend.repositories;

import org.banking.ebanking_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
