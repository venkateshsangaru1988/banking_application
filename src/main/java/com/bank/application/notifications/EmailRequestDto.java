package com.bank.application.notifications;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {

	private String email;
	private String subject;
	private String body;
	private String emailType;
	private String createdBy;
	private LocalDateTime createdDateTime;

}
