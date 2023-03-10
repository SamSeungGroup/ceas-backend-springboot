package com.samseung.ceas.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

@Table(name = "Comments")

public class CommentsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer c_id;
	
	@Column
	private String content;
	
	@Column(name = "created_date")
	@CreatedDate
	private LocalDateTime createdDate;
	
	
	@Column(nullable = false)
	private Integer productId;
	
	@Column
	private String author;
	

}

