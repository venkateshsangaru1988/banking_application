package com.bank.application.service;

import java.util.List;

import com.bank.application.request.dto.AccountDetailsRequestDto;
import com.bank.application.request.dto.AccountInfoRequestDto;
import com.bank.application.request.dto.AuditDetailsRequestDto;
import com.bank.application.request.dto.BankTransactionRequestDto;
import com.bank.application.request.dto.MoneyTransferRequestDto;
import com.bank.application.response.dto.AccountInfoResponseDto;
import com.bank.application.response.dto.AuditInfoResponseDto;

public interface AccountInfoService {

	AccountInfoResponseDto saveAccountInfo(AccountInfoRequestDto requestObj);

	AccountInfoResponseDto doTransaction(BankTransactionRequestDto requestObj);

	AccountInfoResponseDto getAccountDetailsByAccountId(AccountDetailsRequestDto requestObj);

	List<AccountInfoResponseDto> getAllAccountDetails();

	AccountInfoResponseDto moneyTransfer(MoneyTransferRequestDto requestObj);

	List<AuditInfoResponseDto> getMiniStatementByAccountId(AuditDetailsRequestDto requestObj);

}
