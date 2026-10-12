package com.bank.application.request.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditDetailsRequestDto {

	@NotNull(message = "Account no is required")
	private String accountNo;

	private LocalDate fromDate;

	private LocalDate toDate;

	private String auditDuration;
}
