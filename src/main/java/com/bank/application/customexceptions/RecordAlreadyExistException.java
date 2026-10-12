package com.bank.application.customexceptions;

public class RecordAlreadyExistException extends BankApplicationUtilException {

	private static final long serialVersionUID = 1L;

	public RecordAlreadyExistException(String message, int statusCode) {
		super(message, statusCode);
	}

}
