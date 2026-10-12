package com.bank.application.entity.base;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class NotificationBaseEntity {

	@CreatedBy
	@Column(name = "created_by", nullable = false, updatable = false)
	private String createdBy;

	@CreatedDate
	@Column(name = "created_date_time", nullable = false, updatable = false)
	private LocalDateTime createdDateTime;

}