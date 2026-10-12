package com.bank.application.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.application.entity.audit.AccountInfoAudit;

public interface AccountInfoAuditRepository extends JpaRepository<AccountInfoAudit, Long> {

	List<AccountInfoAudit> findByAccountInfoAccountNoOrderByIdDesc(String accountNo);

	List<AccountInfoAudit> findByAccountInfoAccountNoAndTransactionStatusAndCreatedDateTimeBetweenOrderByIdDesc(
			String accountNo, String transactionStatus, LocalDateTime fromCreatedDateTime,
			LocalDateTime toCreatedDateTime);

	List<AccountInfoAudit> findByTransactionStatusAndTransactoinTypeAndAccountInfoIsNotNullAndToAccountInfoIsNotNullAndSchedulerTriggered(
			String transactionStatus, String transactoinType, boolean schedulerTriggered);

	List<AccountInfoAudit> findByAccountInfoAccountNoAndTransactionStatusOrderByIdDesc(String accountNo,
			String transactionStatus);

}
