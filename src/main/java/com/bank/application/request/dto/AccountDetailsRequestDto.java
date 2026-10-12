package com.bank.application.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsRequestDto {

	@NotNull(message = "Account no is required")
	private String accountNo;
}
