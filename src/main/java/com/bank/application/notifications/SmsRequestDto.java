package com.bank.application.notifications;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequestDto {

	private String mobileNo;
	private String subject;
	private String body;
	private String smsType;
	private String smsStatus;
	private String createdBy;
	private LocalDateTime createdDateTime;

}
