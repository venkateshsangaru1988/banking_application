package com.bank.application.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

	@NotBlank(message = "User name is required")
	private String userName;

	@NotBlank(message = "Password is required")
	private String password;
}