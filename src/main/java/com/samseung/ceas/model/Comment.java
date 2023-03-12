package com.samseung.ceas.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String content;

	@Column
	private Double commentPositive;
	
	@Column
	@CreatedDate
	private LocalDateTime createdDate;

	@ManyToOne
	@JsonIgnoreProperties({"comment"})
	@JoinColumn(name="product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User writer;
}

