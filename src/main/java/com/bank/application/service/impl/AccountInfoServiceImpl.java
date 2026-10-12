package com.bank.application.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bank.application.mapper.accountinfo.AccountInfoMapper;
import com.bank.application.request.dto.AccountDetailsRequestDto;
import com.bank.application.request.dto.AccountInfoRequestDto;
import com.bank.application.request.dto.AuditDetailsRequestDto;
import com.bank.application.request.dto.BankTransactionRequestDto;
import com.bank.application.request.dto.MoneyTransferRequestDto;
import com.bank.application.response.dto.AccountInfoResponseDto;
import com.bank.application.response.dto.AuditInfoResponseDto;
import com.bank.application.service.AccountInfoService;

import jakarta.transaction.Transactional;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {

	private final AccountInfoMapper accountInfoMapper;

	AccountInfoServiceImpl(AccountInfoMapper accountInfoMapper) {
		this.accountInfoMapper = accountInfoMapper;
	}

	@Override
	@Transactional
	public AccountInfoResponseDto saveAccountInfo(AccountInfoRequestDto requestObj) {
		return accountInfoMapper.saveAccountInfo(requestObj);
	}

	@Override
	@Transactional
	public AccountInfoResponseDto doTransaction(BankTransactionRequestDto requestObj) {
		return accountInfoMapper.doTransaction(requestObj);
	}

	@Override
	public AccountInfoResponseDto getAccountDetailsByAccountId(AccountDetailsRequestDto requestObj) {
		return accountInfoMapper.getAccountDetailsByAccountId(requestObj);
	}

	@Override
	public List<AccountInfoResponseDto> getAllAccountDetails() {
		return accountInfoMapper.getAllAccountDetails();
	}

	@Override
	@Transactional
	public AccountInfoResponseDto moneyTransfer(MoneyTransferRequestDto requestObj) {
		return accountInfoMapper.moneyTransfer(requestObj);
	}

	@Override
	public List<AuditInfoResponseDto> getMiniStatementByAccountId(AuditDetailsRequestDto requestObj) {
		return accountInfoMapper.getMiniStatementByAccountId(requestObj);
	}

}
