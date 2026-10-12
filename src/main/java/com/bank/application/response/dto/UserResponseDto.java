package com.bank.application.response.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

	private Long id;

	private String userName;

	private String userCustomerId;

	private String mobileNo;

	private String email;

	private LocalDate dateOfBirth;

	private String role;

	private String gender;

	private String address;

	private boolean userIsActive;

	private String createdBy;

	private LocalDateTime createdDateTime;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDateTime;

}
