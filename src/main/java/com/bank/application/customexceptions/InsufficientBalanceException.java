package com.bank.application.customexceptions;

public class InsufficientBalanceException extends BankApplicationUtilException {

	private static final long serialVersionUID = 1L;

	public InsufficientBalanceException(String message, int statusCode) {
		super(message, statusCode);
	}

}
