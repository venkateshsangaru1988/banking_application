package com.bank.application.response.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoResponseDto {

	private Long id;

	private String accountNo;

	private Double balance;

	private String branchCode;

	private String ifscCode;

	private LocalDate openedDate;

	private LocalDate closedDate;

	private LocalDateTime lastTransactionDate;

}
