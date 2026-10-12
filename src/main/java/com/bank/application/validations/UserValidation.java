package com.bank.application.validations;

import org.springframework.stereotype.Component;

import com.bank.application.customexceptions.RecordAlreadyExistException;
import com.bank.application.entity.master.Users;
import com.bank.application.repository.UsersRepository;

@Component
public class UserValidation {

	private final UsersRepository usersRepository;

	UserValidation(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public void emailExistOrNot(String email) {

		Users userEmailExist = usersRepository.findByEmailContainingIgnoreCase(email);
		if (null != userEmailExist) {
			throw new RecordAlreadyExistException("Record already existed with given email id : " + email, 404);
		}
	}

	public void mobileNoExistOrNot(String mobileNo) {

		Users userMobileNoExist = usersRepository.findByMobileNoContainingIgnoreCase(mobileNo);
		if (null != userMobileNoExist) {
			throw new RecordAlreadyExistException("Record already existed with given mobile no : " + mobileNo, 404);
		}
	}

}
