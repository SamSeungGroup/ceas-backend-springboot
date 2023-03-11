package com.samseung.ceas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.samseung.ceas.model.User;
import com.samseung.ceas.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

	public List<User> retrieveAll() {
		List<User> list = userRepository.findAll();
		if (list != null) {
			return userRepository.findAll();
		} else {
			log.warn("User Table is empty");
			throw new IllegalStateException("User Table is empty");
		}
	}

	public User retrieve(final String userId) {
		final User user = userRepository.findById(userId).get();
		if(user != null){
			return user;
		}else{
			log.info(user.getUserId());
			log.info("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		}
    }

	public User update(final User user){
		final Optional<User> original = userRepository.findById(user.getId());
		original.ifPresentOrElse((entity) -> {
			entity.setUserPassword(user.getUserPassword());
			entity.setUserEmail(user.getUserEmail());
			entity.setUserImage(user.getUserImage());
			userRepository.save(entity);
		}, () -> {
			log.warn("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});
		return retrieve(user.getId());
	}

	public List<User> delete(final User user) {
		try {
			userRepository.delete(user);
		} catch (Exception e) {
			log.error("An error occurred while deleting a user", user.getId(), e);
			throw new RuntimeException("An error occurred while deleting a user" + user.getId(), e);
		}
		return userRepository.findAll();
	}

	public String getTempPassword(){
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

		String tempPassword = "";

		// 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
		int idx = 0;
		for (int i = 0; i < 10; i++) {
			idx = (int) (charSet.length * Math.random());
			tempPassword += charSet[idx];
		}
		return tempPassword;
	}
}
