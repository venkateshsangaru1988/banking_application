package com.bank.application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.application.request.dto.UserRequestDto;
import com.bank.application.response.dto.UserResponseDto;
import com.bank.application.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	private final UsersService usersService;

	UserController(UsersService usersService) {
		this.usersService = usersService;
	}

	@Operation(summary = "Get user details By Id", description = "Get personal information")
	@ApiResponse(responseCode = "200", description = "User data fetched successfully")
	@GetMapping("info/{id}")
	public ResponseEntity<UserResponseDto> getUserDetailsById(@PathVariable Long id) {

		log.info("* In side UserController class  getUserDetailsById() method * ");

		UserResponseDto responseObj = usersService.getUserDetailsById(id);

		log.info("* In side UserController class  getUserDetailsById() method after service called * ");

		return new ResponseEntity<>(responseObj, HttpStatus.OK);

	}

	@Operation(summary = "Update user details", description = "Updates personal information")
	@ApiResponse(responseCode = "200", description = "User updated successfully")
	@PutMapping("/update/{id}")
	public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserRequestDto requestObj,
			@PathVariable Long id) {

		log.info("* In side UserController class  updateUser() method * ");

		UserResponseDto responseObj = usersService.updateUser(requestObj, id);

		log.info("* In side UserController class  updateUser() method after service called * ");

		return new ResponseEntity<>(responseObj, HttpStatus.OK);

	}

	@Operation(summary = "Get user details", description = "Get personal information")
	@ApiResponse(responseCode = "200", description = "User data fetched successfully")
	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {

		log.info("* In side UserController class  getAllUsers() method * ");

		List<UserResponseDto> responseList = usersService.getAllUsers();

		log.info("* In side UserController class  getAllUsers() method after service called * ");

		return new ResponseEntity<>(responseList, HttpStatus.OK);

	}

	@Operation(summary = "Update user details", description = "Updates personal information")
	@ApiResponse(responseCode = "200", description = "User updated successfully")
	@DeleteMapping("/in-active/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> userInActive(@PathVariable Long id) {

		log.info("* In side UserController class  userInActive() method * ");

		String responseObj = usersService.userInActive(id);

		log.info("* In side UserController class  userInActive() method after service called * ");

		return new ResponseEntity<>(responseObj, HttpStatus.OK);

	}
}
