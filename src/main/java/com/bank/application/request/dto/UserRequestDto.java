package com.bank.application.request.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

	@NotNull(message = "DOB is required")
	@Past
	private LocalDate dateOfBirth;

	@NotNull(message = "Gender is required")
	private String gender;

	@NotNull(message = "Address is required")
	private String address;

}
