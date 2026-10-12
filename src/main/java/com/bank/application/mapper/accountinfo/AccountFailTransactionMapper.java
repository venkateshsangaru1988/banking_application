package com.bank.application.mapper.accountinfo;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bank.application.entity.audit.AccountInfoAudit;
import com.bank.application.entity.transactions.AccountInfoEntity;
import com.bank.application.repository.AccountInfoAuditRepository;

@Component
public class AccountFailTransactionMapper {

	private final AccountInfoAuditRepository accountInfoAuditRepository;

	AccountFailTransactionMapper(AccountInfoAuditRepository accountInfoAuditRepository) {

		this.accountInfoAuditRepository = accountInfoAuditRepository;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void auditFailSave(AccountInfoEntity accountInfo, String transactoinType, Double balance,
			String transactionStatus) {
		AccountInfoAudit accountInfoAudit = new AccountInfoAudit();
		accountInfoAudit.setBalance(balance);
		accountInfoAudit.setTransactoinType(transactoinType);
		accountInfoAudit.setAccountInfo(accountInfo);
		accountInfoAudit.setCreatedBy(accountInfo.getCreatedBy());
		accountInfoAudit.setCreatedDateTime(LocalDateTime.now());
		accountInfoAudit.setTransactionStatus(transactionStatus);

		accountInfoAuditRepository.save(accountInfoAudit);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void auditFailSaveInMoneyTransfer(AccountInfoEntity fromAccountInfo, AccountInfoEntity toAccountInfo,
			String transactoinType, Double balance, String transactionStatus) {
		AccountInfoAudit accountInfoAudit = new AccountInfoAudit();
		accountInfoAudit.setBalance(balance);
		accountInfoAudit.setTransactoinType(transactoinType);
		accountInfoAudit.setAccountInfo(fromAccountInfo);
		accountInfoAudit.setCreatedBy(fromAccountInfo.getCreatedBy());
		accountInfoAudit.setCreatedDateTime(LocalDateTime.now());
		accountInfoAudit.setTransactionStatus(transactionStatus);
		accountInfoAudit.setToAccountInfo(toAccountInfo);

		accountInfoAuditRepository.save(accountInfoAudit);
	}
}
