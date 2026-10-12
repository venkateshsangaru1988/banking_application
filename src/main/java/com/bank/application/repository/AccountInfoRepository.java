package com.bank.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.application.entity.transactions.AccountInfoEntity;

public interface AccountInfoRepository extends JpaRepository<AccountInfoEntity, Long> {

	AccountInfoEntity findTopByAccountNoStartingWithOrderByIdDesc(String prefix);

	AccountInfoEntity findByAccountNo(String accountNo);

}
