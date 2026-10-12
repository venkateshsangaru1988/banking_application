package com.bank.application.mapper.common;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.bank.application.constants.ApplicationConstants;
import com.bank.application.entity.master.Users;
import com.bank.application.repository.UsersRepository;

@Component
public class CommonMapper {

	private final UsersRepository usersRepository;

	CommonMapper(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public String getUserName() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = null;

		if (null != authentication) {
			userName = authentication.getName();
		} else {
			List<Users> usersList = usersRepository.findAllRoleByOrderByIdDesc(ApplicationConstants.ADMIN);
			if (!usersList.isEmpty()) {
				userName = usersList.get(0).getUserName();
			}
		}

		return userName;
	}
	
	public Users findByUserName(String userName) {
		return usersRepository.findByUserName(userName);
	}

}
