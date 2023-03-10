package com.samseung.ceas.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.samseung.ceas.model.Comment;

import com.samseung.ceas.model.Product;
import com.samseung.ceas.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CommentDTO {
	private Integer commentId;
	private String content;
	private Double commentPositive;
	private LocalDateTime createdDate;
	private Product product;
	private User writer;

    public CommentDTO(final Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.commentPositive = comment.getCommentPositive();
        this.createdDate = comment.getCreatedDate();
		this.product = comment.getProduct();
		this.writer = comment.getWriter();
    }

    public static Comment toEntity(final CommentDTO dto) {
        return Comment.builder()
                .commentId(dto.getCommentId())
				.content(dto.getContent())
                .commentPositive(dto.getCommentPositive())
                .createdDate(dto.getCreatedDate())
				.product(dto.product)
				.writer(dto.writer)
                .build();
    }


}

