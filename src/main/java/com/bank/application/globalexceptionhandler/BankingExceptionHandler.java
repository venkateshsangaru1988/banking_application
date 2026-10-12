package com.bank.application.globalexceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.application.customexceptions.BankApplicationUtilException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class BankingExceptionHandler {

	@ExceptionHandler(BankApplicationUtilException.class)
	public ResponseEntity<ErrorResponse> handleBankingException(BankApplicationUtilException ex,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.valueOf(ex.getStatusCode());

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
				ex.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		String message = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage())
				.findFirst().orElse("Validation failed");

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), message, request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

}
