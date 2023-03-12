package com.samseung.ceas.dto;

import java.time.LocalDateTime;

import com.samseung.ceas.model.Comment;

import com.samseung.ceas.model.Product;
import com.samseung.ceas.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
	private Long id;
	private String content;
	private Double commentPositive;
	private LocalDateTime createdDate;
	private Product product;
	private User writer;

    public CommentDTO(final Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.commentPositive = comment.getCommentPositive();
        this.createdDate = comment.getCreatedDate();
		this.product = comment.getProduct();
		this.writer = comment.getWriter();
    }

    public static Comment toEntity(final CommentDTO dto) {
        return Comment.builder()
                .id(dto.getId())
				.content(dto.getContent())
                .commentPositive(dto.getCommentPositive())
                .createdDate(dto.getCreatedDate())
				.product(dto.product)
				.writer(dto.writer)
                .build();
    }


}

