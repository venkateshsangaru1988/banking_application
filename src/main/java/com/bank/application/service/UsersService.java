package com.bank.application.service;

import java.util.List;

import com.bank.application.request.dto.UserAuthRequestDto;
import com.bank.application.request.dto.UserRequestDto;
import com.bank.application.response.dto.UserResponseDto;

public interface UsersService {

	UserResponseDto saveUser(UserAuthRequestDto requestDto);

	UserResponseDto updateUser(UserRequestDto requestDto, Long id);

	UserResponseDto getUserDetailsById(Long id);

	List<UserResponseDto> getAllUsers();

	String userInActive(Long id);
}
