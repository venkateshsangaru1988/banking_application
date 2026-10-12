package com.bank.application.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey signingKey;
	private final long jwtExpiration;

	public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long jwtExpiration) {

		this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

		this.jwtExpiration = jwtExpiration;

	}

	public String generateToken(UserDetails userDetails) {

		Date now = new Date();

		Date expiry = new Date(now.getTime() + jwtExpiration);

		return Jwts.builder().subject(userDetails.getUsername()).issuedAt(now).expiration(expiry).signWith(signingKey)
				.compact();
	}

	public String extractUsername(String token) {

		String extractUser = extractAllClaims(token).getSubject();
		return extractUser;
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {

		String username = extractUsername(token);

		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {

		return extractAllClaims(token).getExpiration().before(new Date());
	}

	private Claims extractAllClaims(String token) {

		Claims claims = null;
		try {
			claims = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return claims;
	}
}