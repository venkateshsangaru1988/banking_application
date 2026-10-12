package com.bank.application.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.application.constants.ApplicationConstants;
import com.bank.application.entity.master.Users;
import com.bank.application.mapper.users.UserAuthMapper;
import com.bank.application.notifications.EmailNotifications;
import com.bank.application.notifications.EmailRequestDto;
import com.bank.application.notifications.SmsNotifications;
import com.bank.application.notifications.SmsRequestDto;
import com.bank.application.notifications.WhatsAppNotifications;
import com.bank.application.repository.UsersRepository;
import com.bank.application.request.dto.UserAuthRequestDto;
import com.bank.application.request.dto.UserRequestDto;
import com.bank.application.response.dto.UserResponseDto;
import com.bank.application.service.UsersService;

import jakarta.transaction.Transactional;

@Service
public class UsersServiceImpl implements UsersService {

	private final UsersRepository usersRepository;

	private final UserAuthMapper userAuthMapper;

	private final PasswordEncoder passwordEncoder;

	private final SmsNotifications smsNotifications;

	private final EmailNotifications emailNotifications;

	private final WhatsAppNotifications whatsAppNotifications;

	UsersServiceImpl(UsersRepository usersRepository, UserAuthMapper userAuthMapper, PasswordEncoder passwordEncoder,
			SmsNotifications smsNotifications, EmailNotifications emailNotifications,
			WhatsAppNotifications whatsAppNotifications) {
		this.usersRepository = usersRepository;
		this.userAuthMapper = userAuthMapper;
		this.passwordEncoder = passwordEncoder;
		this.smsNotifications = smsNotifications;
		this.emailNotifications = emailNotifications;
		this.whatsAppNotifications = whatsAppNotifications;
	}

	@Override
	@Transactional
	public UserResponseDto saveUser(UserAuthRequestDto requestDto) {

		Users userObj = userAuthMapper.convertDtoToEntity(requestDto);
		// Encode password before saving
		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

		userObj.setPassword(encodedPassword);

		userObj = usersRepository.save(userObj);

		UserResponseDto response = userAuthMapper.convertEntityToDto(userObj);

		sendingNotifications(requestDto, userObj);

		return response;
	}

	private void sendingNotifications(UserAuthRequestDto requestDto, Users userObj) {
		// Sending notifications
		String subject = "Welcome User : " + requestDto.getUserName();
		String body = "Congratulations you are part of the HDFC bank with customer id : " + userObj.getUserCustomerId();
		String createdBy = userObj.getCreatedBy();
		LocalDateTime createdDateTime = userObj.getCreatedDateTime();

		// SMS
		SmsRequestDto smsRequest = new SmsRequestDto();
		smsRequest.setMobileNo(requestDto.getMobileNo());
		smsRequest.setSubject(subject);
		smsRequest.setBody(body);
		smsRequest.setCreatedBy(createdBy);
		smsRequest.setCreatedDateTime(createdDateTime);
		smsRequest.setSmsType(ApplicationConstants.SIGNUP_SCENARIO);
		smsNotifications.sendSms(smsRequest);

		// Email
		EmailRequestDto emailRequest = new EmailRequestDto();
		emailRequest.setEmail(requestDto.getEmail());
		emailRequest.setSubject(subject);
		emailRequest.setBody(body);
		emailRequest.setCreatedBy(createdBy);
		emailRequest.setCreatedDateTime(createdDateTime);
		emailRequest.setEmailType(ApplicationConstants.SIGNUP_SCENARIO);
		emailNotifications.sendEmail(emailRequest);
	}

	@Override
	@Transactional
	public UserResponseDto updateUser(UserRequestDto requestDto, Long id) {
		Users userObj = userAuthMapper.getUserById(id);

		userObj.setDateOfBirth(requestDto.getDateOfBirth());
		userObj.setGender(requestDto.getGender());
		userObj.setAddress(requestDto.getAddress());

		// This should be login user,as of now i have kept DB user only
		userObj.setLastModifiedBy(userObj.getCreatedBy());
		userObj.setLastModifiedDateTime(LocalDateTime.now());

		userObj = usersRepository.save(userObj);

		return userAuthMapper.convertEntityToDto(userObj);
	}

	@Override
	public UserResponseDto getUserDetailsById(Long id) {
		Users userObj = userAuthMapper.getUserById(id);
		return userAuthMapper.convertEntityToDto(userObj);
	}

	@Override
	public List<UserResponseDto> getAllUsers() {

		List<UserResponseDto> responseList = new ArrayList<>();
		List<Users> userList = usersRepository.findAllByOrderByIdDesc();

		if (!userList.isEmpty()) {

			userList.forEach(user -> {
				responseList.add(userAuthMapper.convertEntityToDto(user));
			});

		}

		return responseList;
	}

	@Override
	public String userInActive(Long id) {
		Users userObj = userAuthMapper.getUserById(id);
		userObj.setUserIsActive(Boolean.FALSE);
		userObj = usersRepository.save(userObj);
		return "User is in active";
	}

}
