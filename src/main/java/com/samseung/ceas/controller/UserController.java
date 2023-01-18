package com.samseung.ceas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samseung.ceas.dto.ResponseDTO;
import com.samseung.ceas.dto.UserDTO;
import com.samseung.ceas.model.UserEntity;
import com.samseung.ceas.security.TokenProvider;
import com.samseung.ceas.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
		try {
			if(userDTO == null || userDTO.getPassword() == null) {
				throw new RuntimeException("Invalid Password value");
			}
			UserEntity userEntity = UserEntity.builder()
					.userName(userDTO.getUserName())
					.userId(userDTO.getUserId())
					.password(passwordEncoder.encode(userDTO.getPassword()))
					.email(userDTO.getEmail())
					.build();
			UserEntity registeredUser = userService.create(userEntity);
			UserDTO responseUserDTO = UserDTO.builder()
					.id(registeredUser.getId())
					.userId(registeredUser.getUserId())
					.build();
			return ResponseEntity.ok().body(responseUserDTO);
		}catch (Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
		UserEntity user = userService.getByCredentials(userDTO.getUserId(), userDTO.getPassword(), passwordEncoder);
		if(user != null) {
			final String token = tokenProvider.create(user);
			final UserDTO reponseUserDTO = UserDTO.builder()
					.userId(user.getUserId())
					.id(user.getId())
					.token(token)
					.build();
			return ResponseEntity.ok().body(reponseUserDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
