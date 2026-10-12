package com.bank.application.mapper.users;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bank.application.constants.ApplicationConstants;
import com.bank.application.customexceptions.RecordNotFoundException;
import com.bank.application.entity.master.Users;
import com.bank.application.enums.ApplicationUtilEnum;
import com.bank.application.repository.UsersRepository;
import com.bank.application.request.dto.UserAuthRequestDto;
import com.bank.application.response.dto.UserResponseDto;
import com.bank.application.validations.UserValidation;

@Component
public class UserAuthMapper {

	private final UsersRepository usersRepository;

	private final UserValidation userValidation;

	UserAuthMapper(UsersRepository usersRepository, UserValidation userValidation) {
		this.usersRepository = usersRepository;
		this.userValidation = userValidation;
	}

	public Users getUserById(Long id) {

		Optional<Users> userObj = usersRepository.findById(id);
		if (userObj.isEmpty()) {
			throw new RecordNotFoundException("Record Not exist with given id : " + id, 404);
		}
		return userObj.get();
	}

	public Users convertDtoToEntity(UserAuthRequestDto requestDto) {

		// Performing validations
		userValidation.emailExistOrNot(requestDto.getEmail());
		userValidation.mobileNoExistOrNot(requestDto.getMobileNo());

		Users users = new Users();
		BeanUtils.copyProperties(requestDto, users);

		if (ApplicationConstants.CUSTOMER.equalsIgnoreCase(requestDto.getRole())) {
			users.setRole(ApplicationUtilEnum.CUSTOMER.name());
		} else if (ApplicationConstants.MANAGER.equalsIgnoreCase(requestDto.getRole())) {
			users.setRole(ApplicationUtilEnum.MANAGER.name());
		} else if (ApplicationConstants.ADMIN.equalsIgnoreCase(requestDto.getRole())) {
			users.setRole(ApplicationUtilEnum.ADMIN.name());
		}

		// User is Active
		users.setUserIsActive(Boolean.TRUE);
		// Generating Bank Account number
		users.setUserCustomerId(generateBankAccountNo());
		users.setCreatedBy(requestDto.getUserName());
		users.setCreatedDateTime(LocalDateTime.now());

		return users;
	}

	public UserResponseDto convertEntityToDto(Users users) {

		UserResponseDto responseDto = new UserResponseDto();

		BeanUtils.copyProperties(users, responseDto);

		return responseDto;
	}

	private String generateBankAccountNo() {
		String bankAccountNo = null;
		int currentYear = Year.now().getValue();
		String prefix = ApplicationConstants.BANK_PREFIX + currentYear;

		Users latestUser = usersRepository.findTopByUserCustomerIdStartingWithOrderByUserCustomerIdDesc(prefix);

		int nextSequence = 1;

		if (latestUser != null) {

			String latestCustomerId = latestUser.getUserCustomerId();

			String sequencePart = latestCustomerId.substring(prefix.length());

			nextSequence = Integer.parseInt(sequencePart) + 1;
		}

		bankAccountNo = prefix + String.format("%06d", nextSequence);

		return bankAccountNo;
	}

}
