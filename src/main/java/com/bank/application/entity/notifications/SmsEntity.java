package com.bank.application.entity.notifications;

import com.bank.application.entity.base.NotificationBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sms_audit", schema = "bank_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SmsEntity extends NotificationBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "mobile_no", nullable = false)
	private String mobileNo;

	@Column(name = "subject", nullable = false)
	private String subject;

	@Column(name = "body", nullable = false)
	private String body;

	@Column(name = "sms_status")
	private String smsStatus;

	@Column(name = "sms_type", nullable = false)
	private String smsType;

}
