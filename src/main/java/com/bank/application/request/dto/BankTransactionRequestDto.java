package com.bank.application.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTransactionRequestDto {

	@NotNull(message = "Account type is required")
	private String accountType;

	@NotNull(message = "Transaction type is required")
	private String transactoinType;

	@NotNull(message = "Balance is required")
	private Double balance;

	@NotNull(message = "Account no is required")
	private String accountNo;
}
