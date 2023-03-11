package com.samseung.ceas.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String productName;

	@Column(nullable = false)
	private String productDescription;

	@Column
	private String productImage;

	@Column(nullable = false)
	private Integer productPrice;

	@Column
	private Double productPositive;

	@OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
	private List<Comment> commentList;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@CreatedDate
	private LocalDateTime createdDate;
}
