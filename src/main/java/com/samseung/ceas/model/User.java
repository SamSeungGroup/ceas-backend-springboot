package com.samseung.ceas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private String userPassword;

	@Column(nullable = false)
	private String userEmail;

	@OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"user"})
	private Image userImage;
}
