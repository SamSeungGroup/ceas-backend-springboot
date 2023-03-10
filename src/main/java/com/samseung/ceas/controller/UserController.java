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
import com.samseung.ceas.model.User;
import com.samseung.ceas.security.TokenProvider;
import com.samseung.ceas.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
		try {
			if(userDTO == null || userDTO.getUserPassword() == null) {
				throw new RuntimeException("Invalid Password value");
			}
			User user = User.builder()
					.userName(userDTO.getUserName())
					.userId(userDTO.getUserId())
					.userPassword(passwordEncoder.encode(userDTO.getUserPassword()))
					.userEmail(userDTO.getUserEmail())
					.build();
			User registeredUser = userService.create(user);
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
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
		User user = userService.getByCredentials(userDTO.getUserId(), userDTO.getUserPassword(), passwordEncoder);
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
