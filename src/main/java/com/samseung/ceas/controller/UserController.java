package com.samseung.ceas.controller;

import com.samseung.ceas.dto.ResponseMessage;
import com.samseung.ceas.dto.ResponseDto;
import com.samseung.ceas.model.Image;
import com.samseung.ceas.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.samseung.ceas.dto.ResponseDtos;
import com.samseung.ceas.dto.UserDTO;
import com.samseung.ceas.model.User;
import com.samseung.ceas.security.TokenProvider;
import com.samseung.ceas.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private ImageService imageService;

	@Autowired
	private TokenProvider tokenProvider;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// 회원가입
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
			ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDto);
		}
	}

	// 비밀번호 초기화
	@PutMapping
	public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO){
		try {
			User originUser = userService.retrieveByUserIdAndUserEmail(userDTO.getUserId(), userDTO.getUserEmail());
			String tempPassword = userService.getTempPassword();
			originUser.setUserPassword(passwordEncoder.encode(tempPassword));

			User updatedUser = userService.update(originUser);
			UserDTO userDto = UserDTO.builder()
					.userName(updatedUser.getUserName())
					.userEmail(updatedUser.getUserEmail())
					.userPassword(tempPassword)
					.build();
			ResponseDto<UserDTO> response = ResponseDto.<UserDTO>builder().data(userDto).build();
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDto);
		}
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
		User user = userService.getByCredentials(userDTO.getUserId(), userDTO.getUserPassword(), passwordEncoder);
		if(user != null) {
			final String token = tokenProvider.create(user);
			final UserDTO reponseUserDTO = UserDTO.builder()
					.userName(user.getUserName())
					.userId(user.getUserId())
					.id(user.getId())
					.token(token)
					.build();
			return ResponseEntity.ok().body(reponseUserDTO);
		}else {
			ResponseDto responseDto = ResponseDto.builder().error("Login failed").build();
			return ResponseEntity.badRequest().body(responseDto);
		}
	}

	// 내 정보 조회
	@GetMapping("/{id}")
	public ResponseEntity<?> retrieveByUserId(@AuthenticationPrincipal String userId, @PathVariable("id") String id){
		try{
			if(userService.retrieve(userId).getId().equals(id)){
				User user = userService.retrieve(userId);
				UserDTO dto = UserDTO.builder()
						.userName(user.getUserName())
						.userId(user.getUserId())
						.userImage(user.getUserImage())
						.userEmail(user.getUserEmail())
						.impId(user.getImpId())
						.pgId(user.getPgId())
						.build();
				ResponseDto response = ResponseDto.builder().data(dto).build();
				return ResponseEntity.ok().body(response);
			}else{
				ResponseDto responseDto = ResponseDto.builder().error("자신의 정보만 볼 수 있습니다.").build();
				return ResponseEntity.badRequest().body(responseDto);
			}
		}catch (NoSuchElementException e){
			ResponseDto responseDto = ResponseDto.builder().error("Entity is not existed").build();
			return ResponseEntity.badRequest().body(responseDto);
		}catch (Exception e){
			ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDto);
		}
	}

	// 비밀번호 변경
	@PostMapping("/{id}")
	public ResponseEntity<?> updatePassword(@AuthenticationPrincipal String userId, @PathVariable("id") String id, @RequestBody UserDTO userDTO){
		try {
			if(userService.retrieve(userId).getId().equals(id)){
				User originUser = userService.retrieve(userId);
				originUser.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
				User updatedUser = userService.update(originUser);
				UserDTO userDto = UserDTO.builder()
						.id(updatedUser.getId())
						.userId(updatedUser.getUserId())
						.build();
				ResponseDto<UserDTO> response = ResponseDto.<UserDTO>builder().data(userDto).build();
				return ResponseEntity.ok().body(response);
			}else {
				ResponseDto responseDto = ResponseDto.builder().error("자신의 정보만 수정할 수 있습니다.").build();
				return ResponseEntity.badRequest().body(responseDto);
			}
		}catch (Exception e) {
			ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDto);
		}
	}

	// 내 정보 수정
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@AuthenticationPrincipal String userId,
										@PathVariable("id") String id,
                                        @RequestPart("dto") UserDTO userDTO,
                                        @Validated @RequestPart("image") List<MultipartFile> files){
		try {
			if(userService.retrieve(userId).getId().equals(id)){
				User originUser = userService.retrieve(userId);
				Image userImage = originUser.getUserImage();

				Image image;
				if(userImage!=null){
					image = imageService.findByUserId(userId);
				}else{
					image = Image.builder().build();
				}
				imageService.addUserImage(image, files, userDTO);
				Image savedImage = imageService.findByUserId(id);

				originUser.setUserImage(savedImage);
				originUser.setUserName(userDTO.getUserName());
				originUser.setUserEmail(userDTO.getUserEmail());
				originUser.setImpId(userDTO.getImpId());
				originUser.setPgId(userDTO.getPgId());

				User savedUser = userService.update(originUser);

				UserDTO userDto = new UserDTO(savedUser);
				ResponseDto<UserDTO> response = ResponseDto.<UserDTO>builder().data(userDto).build();
				return ResponseEntity.ok().body(response);
			}else {
				ResponseDto responseDto = ResponseDto.builder().error("자신의 정보만 수정할 수 있습니다.").build();
				return ResponseEntity.badRequest().body(responseDto);
			}
		}catch (Exception e) {
			ResponseDto responseDto = ResponseDto.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDto);
		}
	}

	// 회원 탈퇴
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@AuthenticationPrincipal String userId, @PathVariable("id") String id, @RequestHeader("User-password") String inputPassword){
		try {
			User user = userService.retrieve(userId);
			if(user.getId().equals(id)){
				if(passwordEncoder.matches(inputPassword, user.getUserPassword())){
					userService.delete(user);
					if(user.getUserImage() != null){
						File file = new File(user.getUserImage().getStoredFileName());
						file.delete();
					}
					ResponseDto<UserDTO> response = ResponseDto.<UserDTO>builder()
							.code(ResponseMessage.SUCCESS)
							.build();
					return ResponseEntity.ok().body(response);
				}else{
					ResponseDto responseDto = ResponseDto.builder()
							.code(ResponseMessage.FAIL)
							.error("비밀번호가 틀립니다.")
							.build();
					return ResponseEntity.badRequest().body(responseDto);
				}
			}else {
				ResponseDto responseDto = ResponseDto.builder()
						.code(ResponseMessage.FAIL)
						.error("자신의 정보만 삭제할 수 있습니다.")
						.build();
				return ResponseEntity.badRequest().body(responseDto);
			}
		}catch (Exception e) {
			ResponseDtos<UserDTO> response = ResponseDtos.<UserDTO>builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(response);
		}
	}

}
