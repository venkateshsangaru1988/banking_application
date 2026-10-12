package com.bank.application.customexceptions;

public class BankApplicationUtilException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int statusCode;

	public BankApplicationUtilException(String message, int statusCode) {

		super(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
