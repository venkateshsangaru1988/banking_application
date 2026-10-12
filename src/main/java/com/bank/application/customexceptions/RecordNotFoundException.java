package com.bank.application.customexceptions;

public class RecordNotFoundException extends BankApplicationUtilException {

	private static final long serialVersionUID = 1L;

	public RecordNotFoundException(String message, int statusCode) {
		super(message, statusCode);
	}

}
