package com.bank.application.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bank.application.request.dto.LoginRequestDto;
import com.bank.application.response.dto.LoginResponseDto;
import com.bank.application.security.CustomUserDetailsService;
import com.bank.application.security.JwtService;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final CustomUserDetailsService userDetailsService;

	public AuthService(

			AuthenticationManager authenticationManager, JwtService jwtService,
			CustomUserDetailsService userDetailsService) {

		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;

	}

	public LoginResponseDto login(LoginRequestDto request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String token = jwtService.generateToken(userDetails);

		return new LoginResponseDto(token, "Bearer", 3600000);
	}
}
