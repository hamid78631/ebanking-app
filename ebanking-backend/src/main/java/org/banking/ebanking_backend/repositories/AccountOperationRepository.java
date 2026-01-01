package org.banking.ebanking_backend.repositories;

import org.banking.ebanking_backend.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // <-- CORRECTION : Import de Spring Data
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

    List<AccountOperation> findByBankAccountId(String bankAccountId);

    // Spring utilisera maintenant cet objet pour générer les clauses "LIMIT" et "OFFSET" en SQL
    Page<AccountOperation> findByBankAccountId(String bankAccountId, Pageable pageable);
}