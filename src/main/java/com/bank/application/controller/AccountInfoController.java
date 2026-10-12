package com.bank.application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.application.request.dto.AccountDetailsRequestDto;
import com.bank.application.request.dto.AccountInfoRequestDto;
import com.bank.application.request.dto.BankTransactionRequestDto;
import com.bank.application.request.dto.MoneyTransferRequestDto;
import com.bank.application.response.dto.AccountInfoResponseDto;
import com.bank.application.service.AccountInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/account-info")
public class AccountInfoController {

	private final AccountInfoService accountInfoService;

	AccountInfoController(AccountInfoService accountInfoService) {
		this.accountInfoService = accountInfoService;
	}

	@Operation(summary = "Account Info saving", description = "Creates a new account in the system")
	@ApiResponse(responseCode = "201", description = "Account created successfully")
	@PostMapping("/create")
	public ResponseEntity<AccountInfoResponseDto> saveAccountInfo(
			@Valid @RequestBody AccountInfoRequestDto requestObj) {
		AccountInfoResponseDto responseObj = accountInfoService.saveAccountInfo(requestObj);
		return new ResponseEntity<>(responseObj, HttpStatus.CREATED);

	}

	@Operation(summary = "Transaction details", description = "Balance updated to respective account")
	@ApiResponse(responseCode = "200", description = "Account Updated successfully")
	@PutMapping("/do-txn")
	public ResponseEntity<AccountInfoResponseDto> doTransaction(
			@Valid @RequestBody BankTransactionRequestDto requestObj) {
		AccountInfoResponseDto responseObj = accountInfoService.doTransaction(requestObj);
		return new ResponseEntity<>(responseObj, HttpStatus.OK);

	}

	@Operation(summary = "Get Account details", description = "Get Account details by account id")
	@ApiResponse(responseCode = "200", description = "Account fetched successfully")
	@GetMapping("/get-account-details-by-account-id")
	public ResponseEntity<AccountInfoResponseDto> getAccountDetailsByAccountId(
			@Valid @RequestBody AccountDetailsRequestDto requestObj) {
		AccountInfoResponseDto responseObj = accountInfoService.getAccountDetailsByAccountId(requestObj);
		return new ResponseEntity<>(responseObj, HttpStatus.OK);

	}

	@Operation(summary = "Get all Account details", description = "Get all Account details")
	@ApiResponse(responseCode = "200", description = "Account details fetched successfully")
	@GetMapping("/get-all-account-details")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<AccountInfoResponseDto>> getAllAccountDetails() {
		List<AccountInfoResponseDto> responseList = accountInfoService.getAllAccountDetails();
		return new ResponseEntity<>(responseList, HttpStatus.OK);

	}

	@Operation(summary = "Amount Transfer details", description = "Amount updated to respective account")
	@ApiResponse(responseCode = "200", description = "Account Updated successfully")
	@PutMapping("/do-money-transfer")
	public ResponseEntity<AccountInfoResponseDto> moneyTransfer(
			@Valid @RequestBody MoneyTransferRequestDto requestObj) {
		AccountInfoResponseDto responseObj = accountInfoService.moneyTransfer(requestObj);
		return new ResponseEntity<>(responseObj, HttpStatus.OK);

	}

}
