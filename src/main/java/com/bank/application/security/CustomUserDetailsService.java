package com.bank.application.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bank.application.entity.master.Users;
import com.bank.application.repository.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UsersRepository usersRepository;

	public CustomUserDetailsService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Users user = usersRepository.findByUserName(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found: " + username);
		}

		return User.builder().username(user.getUserName()).password(user.getPassword()).roles(user.getRole())
				.disabled(!user.isUserIsActive()).build();
	}
}