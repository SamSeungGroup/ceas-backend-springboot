package com.samseung.ceas.dto;

import com.samseung.ceas.model.Image;
import com.samseung.ceas.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private String token;
	private String userName;
	private String userId;
	private String userPassword;
	private String userEmail;
	private Image userImage;
	private String id;

	public UserDTO(final User user) {
		this.id = user.getId();
		this.userName = user.getUserName();
		this.userId = user.getUserId();
		this.userPassword = user.getUserPassword();
		this.userEmail = user.getUserEmail();
		this.userImage = user.getUserImage();
	}

	public static User toEntity(final UserDTO dto) {
		return User.builder()
				.id(dto.getId())
				.userName(dto.getUserName())
				.userId(dto.getUserId())
				.userPassword(dto.getUserPassword())
				.userEmail(dto.getUserEmail())
				.build();
	}
}
