package com.bank.application.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferRequestDto {

	@NotNull(message = "From Account no is required")
	private String fromAccountNo;

	@NotNull(message = "To Account no is required")
	private String toAccountNo;

	@NotNull(message = "Amount is required")
	private Double transferAmount;
}
