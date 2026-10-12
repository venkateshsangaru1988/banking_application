package com.bank.application.entity.audit;

import com.bank.application.entity.base.BaseEntity;
import com.bank.application.entity.transactions.AccountInfoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_info_audit", schema = "bank_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountInfoAudit extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "balance", nullable = false)
	private Double balance;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_info_id", nullable = false)
	private AccountInfoEntity accountInfo;

	@Column(name = "transaction_type")
	private String transactoinType;

	@Column(name = "transaction_status")
	private String transactionStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_account_info_id")
	private AccountInfoEntity toAccountInfo;

	@Column(name = "is_scheduler", nullable = false)
	private boolean schedulerTriggered = false;

}
