package com.samseung.ceas.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.samseung.ceas.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenProvider {
	private static final String SECRET_KEY = "VsgJ2sdfF34sdNfd4Vdfy5OdfgDsa73Sadf1Y29dE3asBdbsSe";
	
	public String create(UserEntity userEntity) {
		Date expiryDate = Date.from(
				Instant.now()
				.plus(1, ChronoUnit.DAYS));
		
		return Jwts.builder()
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				.setSubject(userEntity.getId())
				.setIssuer("ceas")
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.compact();
	}
	
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
}
