package com.bank.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.application.entity.master.AccountTypeEntity;

public interface AccountTypeRepository extends JpaRepository<AccountTypeEntity, Long> {

	AccountTypeEntity findByAccountType(String accountType);

}
