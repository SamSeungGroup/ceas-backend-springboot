package com.samseung.ceas.dto;

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
	private String userImage;
	private String id;

	
	
	
	
}
