package com.bank.application.mapper.accountinfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bank.application.constants.ApplicationConstants;
import com.bank.application.customexceptions.InsufficientBalanceException;
import com.bank.application.customexceptions.RecordNotFoundException;
import com.bank.application.entity.audit.AccountInfoAudit;
import com.bank.application.entity.master.AccountTypeEntity;
import com.bank.application.entity.transactions.AccountInfoEntity;
import com.bank.application.enums.TransactionTypeEnum;
import com.bank.application.mapper.common.CommonMapper;
import com.bank.application.notifications.EmailNotifications;
import com.bank.application.notifications.EmailRequestDto;
import com.bank.application.notifications.SmsNotifications;
import com.bank.application.notifications.SmsRequestDto;
import com.bank.application.repository.AccountInfoAuditRepository;
import com.bank.application.repository.AccountInfoRepository;
import com.bank.application.repository.AccountTypeRepository;
import com.bank.application.request.dto.AccountDetailsRequestDto;
import com.bank.application.request.dto.AccountInfoRequestDto;
import com.bank.application.request.dto.AuditDetailsRequestDto;
import com.bank.application.request.dto.BankTransactionRequestDto;
import com.bank.application.request.dto.MoneyTransferRequestDto;
import com.bank.application.response.dto.AccountInfoResponseDto;
import com.bank.application.response.dto.AuditInfoResponseDto;

@Component
public class AccountInfoMapper {

	private final AccountInfoRepository accountInfoRepository;

	private final AccountTypeRepository accountTypeRepository;

	private final AccountInfoAuditRepository accountInfoAuditRepository;

	private final AccountFailTransactionMapper accountFailTransactionMapper;

	private final CommonMapper commonMapper;

	private final SmsNotifications smsNotifications;

	private final EmailNotifications emailNotifications;

	AccountInfoMapper(AccountInfoRepository accountInfoRepository, AccountTypeRepository accountTypeRepository,
			AccountInfoAuditRepository accountInfoAuditRepository,
			AccountFailTransactionMapper accountFailTransactionMapper, CommonMapper commonMapper,
			SmsNotifications smsNotifications, EmailNotifications emailNotifications) {
		this.accountInfoRepository = accountInfoRepository;
		this.accountTypeRepository = accountTypeRepository;
		this.accountInfoAuditRepository = accountInfoAuditRepository;
		this.accountFailTransactionMapper = accountFailTransactionMapper;
		this.commonMapper = commonMapper;
		this.smsNotifications = smsNotifications;
		this.emailNotifications = emailNotifications;
	}

	public AccountInfoResponseDto saveAccountInfo(AccountInfoRequestDto requestObj) {
		AccountInfoEntity accountInfo = new AccountInfoEntity();
		accountInfo.setAccountNo(generateAccountNo());
		AccountTypeEntity accountType = accountTypeRepository.findByAccountType(requestObj.getAccountType());
		if (null != accountType) {
			accountInfo.setAccountType(accountType);
		}
		accountInfo.setBalance(requestObj.getBalance());
		accountInfo.setBranchCode(ApplicationConstants.BRANCH_CODE);
		accountInfo.setIfscCode(ApplicationConstants.IFSCCODE);
		accountInfo.setOpenedDate(LocalDate.now());
		accountInfo.setCreatedBy(commonMapper.getUserName());
		
		// Setting user id
		accountInfo.setUsers(commonMapper.findByUserName(accountInfo.getCreatedBy()));
		
		accountInfo.setCreatedDateTime(LocalDateTime.now());
		accountInfo = accountInfoRepository.save(accountInfo);
		// Audit logic
		auditSave(accountInfo, null, requestObj.getBalance(), ApplicationConstants.SUCCESS);
		return convertEntityToDto(accountInfo);
	}

	private String generateAccountNo() {
		String bankAccountNo = null;
		int currentYear = Year.now().getValue();
		String prefix = currentYear + "";

		AccountInfoEntity latestAccountNo = accountInfoRepository.findTopByAccountNoStartingWithOrderByIdDesc(prefix);

		int nextSequence = 1;

		if (latestAccountNo != null) {

			String latestCustomerId = latestAccountNo.getAccountNo();

			String sequencePart = latestCustomerId.substring(prefix.length());

			nextSequence = Integer.parseInt(sequencePart) + 1;
		}

		bankAccountNo = prefix + String.format("%06d", nextSequence);

		return bankAccountNo;
	}

	private AccountInfoResponseDto convertEntityToDto(AccountInfoEntity accountInfo) {

		AccountInfoResponseDto dto = new AccountInfoResponseDto();
		BeanUtils.copyProperties(accountInfo, dto);
		return dto;
	}

	public void auditSave(AccountInfoEntity accountInfo, String transactoinType, Double balance,
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

	public AccountInfoResponseDto doTransaction(BankTransactionRequestDto requestObj) {
		AccountInfoEntity accountInfo = null;
		try {
			accountInfo = accountInfoRepository.findByAccountNo(requestObj.getAccountNo());
			if (null == accountInfo) {
				throw new RecordNotFoundException(
						"Record not found with given account no : " + requestObj.getAccountNo(), 404);
			}

			double balanceAmount = accountInfo.getBalance();

			if (TransactionTypeEnum.CREDIT.toString().equalsIgnoreCase(requestObj.getTransactoinType())) {

				balanceAmount = balanceAmount + requestObj.getBalance();

			} else if (TransactionTypeEnum.DEBIT.toString().equalsIgnoreCase(requestObj.getTransactoinType())) {
				// Doing Debit logic

				if (balanceAmount < requestObj.getBalance()) {

					throw new InsufficientBalanceException(
							"Amount insufficient with given account no : " + requestObj.getAccountNo(), 404);
				}

				balanceAmount = balanceAmount - requestObj.getBalance();
			}

			accountInfo.setBalance(balanceAmount);
			accountInfo.setLastModifiedBy(commonMapper.getUserName());
			accountInfo.setLastModifiedDateTime(LocalDateTime.now());
			accountInfo.setLastTransactionDate(LocalDateTime.now());

			accountInfo = accountInfoRepository.save(accountInfo);
			// Audit Logic
			auditSave(accountInfo, requestObj.getTransactoinType(), requestObj.getBalance(),
					ApplicationConstants.SUCCESS);

			// Sending notifications
			sendingNotifications(requestObj, accountInfo);
			return convertEntityToDto(accountInfo);

		} catch (Exception e) {
			// Audit Logic
			accountFailTransactionMapper.auditFailSave(accountInfo, requestObj.getTransactoinType(),
					requestObj.getBalance(), ApplicationConstants.FAIL);
			throw e;
		}

	}

	private void sendingNotifications(BankTransactionRequestDto requestDto, AccountInfoEntity accountInfo) {
		// Sending notifications
		String subject = "Welcome User : " + accountInfo.getUsers().getUserName();
		String body = "Congratulations you are part of the HDFC bank with customer id : "
				+ accountInfo.getUsers().getUserCustomerId();
		String createdBy = accountInfo.getCreatedBy();
		LocalDateTime createdDateTime = accountInfo.getCreatedDateTime();

		// SMS
		SmsRequestDto smsRequest = new SmsRequestDto();
		smsRequest.setMobileNo(accountInfo.getUsers().getMobileNo());
		smsRequest.setSubject(subject);
		smsRequest.setBody(body);
		smsRequest.setCreatedBy(createdBy);
		smsRequest.setCreatedDateTime(createdDateTime);
		smsRequest.setSmsType(requestDto.getTransactoinType());
		smsNotifications.sendSms(smsRequest);

		// Email
		EmailRequestDto emailRequest = new EmailRequestDto();
		emailRequest.setEmail(accountInfo.getUsers().getEmail());
		emailRequest.setSubject(subject);
		emailRequest.setBody(body);
		emailRequest.setCreatedBy(createdBy);
		emailRequest.setCreatedDateTime(createdDateTime);
		emailRequest.setEmailType(requestDto.getTransactoinType());
		emailNotifications.sendEmail(emailRequest);
	}

	public AccountInfoResponseDto getAccountDetailsByAccountId(AccountDetailsRequestDto requestObj) {

		AccountInfoEntity accountInfo = accountInfoRepository.findByAccountNo(requestObj.getAccountNo());
		if (null == accountInfo) {
			throw new RecordNotFoundException("Record not found with given account no : " + requestObj.getAccountNo(),
					404);
		}
		return convertEntityToDto(accountInfo);
	}

	public List<AccountInfoResponseDto> getAllAccountDetails() {

		List<AccountInfoResponseDto> responseList = new ArrayList<>();

		List<AccountInfoEntity> accountInfoList = accountInfoRepository.findAll();
		if (!accountInfoList.isEmpty()) {

			accountInfoList.forEach(accountInfo -> {
				responseList.add(convertEntityToDto(accountInfo));
			});

		}

		return responseList;

	}

	public AccountInfoResponseDto moneyTransfer(MoneyTransferRequestDto requestObj) {

		AccountInfoEntity fromAccountInfo = null;
		AccountInfoEntity toAccountInfo = null;

		try {
			fromAccountInfo = accountInfoRepository.findByAccountNo(requestObj.getFromAccountNo());
			if (null == fromAccountInfo) {
				throw new RecordNotFoundException(
						"Record not found with given from account no : " + requestObj.getFromAccountNo(), 404);
			}

			toAccountInfo = accountInfoRepository.findByAccountNo(requestObj.getToAccountNo());
			if (null == toAccountInfo) {
				throw new RecordNotFoundException(
						"Record not found with given to account no : " + requestObj.getToAccountNo(), 404);
			}
			double balanceAmount = fromAccountInfo.getBalance();
			if (balanceAmount < requestObj.getTransferAmount()) {

				throw new InsufficientBalanceException(
						"Amount insufficient with given account no : " + requestObj.getFromAccountNo(), 404);
			}

			fromAccountInfo.setBalance(balanceAmount - requestObj.getTransferAmount());
			fromAccountInfo.setLastModifiedBy(commonMapper.getUserName());
			fromAccountInfo.setLastModifiedDateTime(LocalDateTime.now());
			fromAccountInfo.setLastTransactionDate(LocalDateTime.now());

			fromAccountInfo = accountInfoRepository.save(fromAccountInfo);
			// Audit Logic Save for from account
			auditSave(fromAccountInfo, TransactionTypeEnum.TRANSFER.toString(), requestObj.getTransferAmount(),
					ApplicationConstants.SUCCESS);

			toAccountInfo.setBalance(toAccountInfo.getBalance() + requestObj.getTransferAmount());
			toAccountInfo.setLastModifiedBy(commonMapper.getUserName());
			toAccountInfo.setLastModifiedDateTime(LocalDateTime.now());
			toAccountInfo.setLastTransactionDate(LocalDateTime.now());

			toAccountInfo = accountInfoRepository.save(toAccountInfo);

			// Audit Logic Save for to account
			auditSave(toAccountInfo, TransactionTypeEnum.TRANSFER.toString(), requestObj.getTransferAmount(),
					ApplicationConstants.SUCCESS);

			return convertEntityToDto(fromAccountInfo);
		} catch (Exception e) {

			// Audit Logic
			accountFailTransactionMapper.auditFailSaveInMoneyTransfer(fromAccountInfo, toAccountInfo,
					TransactionTypeEnum.TRANSFER.toString(), requestObj.getTransferAmount(), ApplicationConstants.FAIL);
			throw e;
		}

	}

	public List<AuditInfoResponseDto> getMiniStatementByAccountId(AuditDetailsRequestDto requestObj) {

		List<AuditInfoResponseDto> responseList = new ArrayList<>();
		List<AccountInfoAudit> auditList = null;

		if (null != requestObj.getFromDate() && null != requestObj.getToDate()) {
			LocalDateTime fromDateTime = requestObj.getFromDate().atStartOfDay();

			LocalDateTime toDateTime = requestObj.getToDate().atStartOfDay();

			auditList = accountInfoAuditRepository
					.findByAccountInfoAccountNoAndTransactionStatusAndCreatedDateTimeBetweenOrderByIdDesc(
							requestObj.getAccountNo(), ApplicationConstants.SUCCESS, fromDateTime, toDateTime);
		} else if (null != requestObj.getAuditDuration()) {
			String auditDuration = requestObj.getAuditDuration();

			// Start Time of the Day
			LocalDateTime fromDateTime = LocalDate.now().atStartOfDay();

			// End Time of the Day
			LocalDateTime toDateTime = LocalDate.now().atTime(LocalTime.MAX);

			if (ApplicationConstants.MONTHLY.equalsIgnoreCase(auditDuration)) {

				fromDateTime = fromDateTime.minusDays(30);
				toDateTime = toDateTime.plusDays(30);

			} else if (ApplicationConstants.QUARTERLY.equalsIgnoreCase(auditDuration)) {

				fromDateTime = fromDateTime.minusDays(90);
				toDateTime = toDateTime.plusDays(90);

			} else if (ApplicationConstants.ANNUALLY.equalsIgnoreCase(auditDuration)) {
				fromDateTime = fromDateTime.minusYears(1);
			}
			// By default it is DAY
			auditList = accountInfoAuditRepository
					.findByAccountInfoAccountNoAndTransactionStatusAndCreatedDateTimeBetweenOrderByIdDesc(
							requestObj.getAccountNo(), ApplicationConstants.SUCCESS, fromDateTime, toDateTime);
		} else {
			auditList = accountInfoAuditRepository.findByAccountInfoAccountNoAndTransactionStatusOrderByIdDesc(
					requestObj.getAccountNo(), ApplicationConstants.SUCCESS);
		}

		if (!auditList.isEmpty()) {

			auditList.forEach(audit -> {
				AuditInfoResponseDto dto = auditConvertEntityToDto(audit);
				responseList.add(dto);

			});
		}
		return responseList;
	}

	private AuditInfoResponseDto auditConvertEntityToDto(AccountInfoAudit audit) {
		AuditInfoResponseDto dto = new AuditInfoResponseDto();
		AccountInfoEntity accountInfo = audit.getAccountInfo();
		dto.setId(audit.getId());
		dto.setBalance(audit.getBalance());
		dto.setAccountNo(accountInfo.getAccountNo());
		dto.setBranchCode(accountInfo.getBranchCode());
		dto.setIfscCode(accountInfo.getIfscCode());
		dto.setOpenedDate(accountInfo.getOpenedDate());
		dto.setClosedDate(accountInfo.getClosedDate());
		dto.setLastTransactionDate(audit.getLastModifiedDateTime());
		dto.setTransactionStatus(audit.getTransactionStatus());
		dto.setTransactoinType(audit.getTransactoinType());
		return dto;
	}

}
