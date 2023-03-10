package com.samseung.ceas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.samseung.ceas.model.User;
import com.samseung.ceas.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public User create(final User user) {
		if(user == null || user.getUserId() == null) {
			throw new RuntimeException("Invalid arguments");
		}
		
		final String userId = user.getUserId();
		if(userRepository.existsByUserId(userId)) {
			log.warn("UserId already exists {}", userId);
			throw new RuntimeException("UserId already exists");
		}
		return userRepository.save(user);
	}
	
	public User getByCredentials(final String userId, final String password, final PasswordEncoder encoder) {
		final User originalUser = userRepository.findByUserId(userId);
		if(originalUser != null && encoder.matches(password, originalUser.getUserPassword())) {
			return originalUser;
		}
		
		return null;
	}
	
	public User getByUserId(final String userId) {
        return userRepository.findByUserId(userId);
    }
	
}
