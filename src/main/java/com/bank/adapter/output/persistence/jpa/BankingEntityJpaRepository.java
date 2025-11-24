package com.bank.adapter.output.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface BankingEntityJpaRepository extends JpaRepository<BankingEntityJpaEntity, Long> {
    Optional<BankingEntityJpaEntity> findByCode(String code);

    List<BankingEntityJpaEntity> findByType(String type);
}