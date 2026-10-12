package com.bank.application.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bank.application.constants.ApplicationConstants;
import com.bank.application.entity.audit.AccountInfoAudit;
import com.bank.application.entity.configuration.SchedulerEntity;
import com.bank.application.enums.TransactionTypeEnum;
import com.bank.application.repository.AccountInfoAuditRepository;
import com.bank.application.repository.SchedulerRepository;
import com.bank.application.request.dto.MoneyTransferRequestDto;
import com.bank.application.service.AccountInfoService;

import jakarta.transaction.Transactional;

@Component
public class SchedulerJob {

	private final SchedulerRepository schedulerRepository;
	private final AccountInfoAuditRepository accountInfoAuditRepository;
	private final AccountInfoService accountInfoService;

	SchedulerJob(SchedulerRepository schedulerRepository, AccountInfoAuditRepository accountInfoAuditRepository,
			AccountInfoService accountInfoService) {
		this.schedulerRepository = schedulerRepository;
		this.accountInfoAuditRepository = accountInfoAuditRepository;
		this.accountInfoService = accountInfoService;
	}

	@Scheduled(cron = "${scheduler.cron}")
	@Transactional
	public void failedRecordsJob() {

		List<SchedulerEntity> schedulerList = schedulerRepository.findAll();
		if (!schedulerList.isEmpty() && schedulerList.get(0).getSchedulerActive().equalsIgnoreCase("true")) {

			List<AccountInfoAudit> auditList = accountInfoAuditRepository
					.findByTransactionStatusAndTransactoinTypeAndAccountInfoIsNotNullAndToAccountInfoIsNotNullAndSchedulerTriggered(
							ApplicationConstants.FAIL, TransactionTypeEnum.TRANSFER.toString(), Boolean.FALSE);

			if (!auditList.isEmpty()) {
				auditList.forEach(auditRecord -> {
					MoneyTransferRequestDto requestObj = new MoneyTransferRequestDto();
					requestObj.setFromAccountNo(auditRecord.getAccountInfo().getAccountNo());
					requestObj.setToAccountNo(auditRecord.getToAccountInfo().getAccountNo());
					requestObj.setTransferAmount(auditRecord.getBalance());

					// Calling service method
					accountInfoService.moneyTransfer(requestObj);

					// Setting Scheduler triggered
					auditRecord.setSchedulerTriggered(Boolean.TRUE);
					accountInfoAuditRepository.save(auditRecord);
				});
			}

		}
	}

}
