package com.samseung.ceas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.samseung.ceas.model.UserEntity;
import com.samseung.ceas.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public UserEntity create(final UserEntity userEntity) {
		if(userEntity == null || userEntity.getUserId() == null) {
			throw new RuntimeException("Invalid arguments");
		}
		final String userId = userEntity.getUserId();
		if(userRepository.existsByUserId(userId)) {
			log.warn("UserId already exists {}", userId);
			throw new RuntimeException("UserId already exists");
		}
		return userRepository.save(userEntity);
	}
	
	public UserEntity getByCredentials(final String userId, final String password, final PasswordEncoder encoder) {
		final UserEntity originalUser = userRepository.findByUserId(userId);
		originalUser.setPassword(encoder.encode(originalUser.getPassword()));
		if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
			return originalUser;
		}
		
		return null;
	}
}
