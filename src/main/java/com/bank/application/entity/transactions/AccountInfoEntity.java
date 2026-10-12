package com.bank.application.entity.transactions;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bank.application.entity.base.BaseEntity;
import com.bank.application.entity.master.AccountTypeEntity;
import com.bank.application.entity.master.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_info", schema = "bank_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountInfoEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "account_no", nullable = false, unique = true)
	private String accountNo;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private Users users;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_type_id", nullable = false)
	private AccountTypeEntity accountType;

	@Column(name = "balance", nullable = false)
	private Double balance;

	@Column(name = "branch_code", length = 20)
	private String branchCode;

	@Column(name = "ifsc_code", length = 20)
	private String ifscCode;

	@Column(name = "opened_date")
	private LocalDate openedDate;

	@Column(name = "closed_date")
	private LocalDate closedDate;

	@Column(name = "last_transaction_date")
	private LocalDateTime lastTransactionDate;

}
