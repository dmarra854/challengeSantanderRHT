package com.bank.adapter.output.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Spring Data JPA repository.
 * Single Responsibility: Provides JPA operations only
 */
@Repository
public interface BankAccountJpaRepository extends JpaRepository<BankAccountJpaEntity, Long> {
    Optional<BankAccountJpaEntity> findByAccountNumber(String accountNumber);
    List<BankAccountJpaEntity> findByAccountHolder(String accountHolder);
}
