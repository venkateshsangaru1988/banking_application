package com.bank.application.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoRequestDto {

	@NotNull(message = "Account type is required")
	private String accountType;

	@NotNull(message = "Balance is required")
	private Double balance;
}
