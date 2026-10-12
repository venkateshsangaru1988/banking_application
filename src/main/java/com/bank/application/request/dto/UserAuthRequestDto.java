package com.bank.application.request.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRequestDto {

	@NotNull(message = "User name is required")
	private String userName;

	@NotNull(message = "Password is required")
	private String password;

	@NotNull(message = "Mobile no is required")
	private String mobileNo;

	@NotNull(message = "Email is required")
	@Email
	private String email;

	@NotNull(message = "DOB is required")
	@Past
	private LocalDate dateOfBirth;

	@NotNull(message = "Role is required")
	private String role;

	@NotNull(message = "Gender is required")
	private String gender;

	@NotNull(message = "Address is required")
	private String address;

}
