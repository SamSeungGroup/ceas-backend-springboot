package com.samseung.ceas.dto;

import java.time.LocalDateTime;

import com.samseung.ceas.model.CommentsEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
	
	public class CommentsDTO {
		private Integer c_id;
		private String author;
		private String content;
		private Integer productId;
		private LocalDateTime createdDate;
		

		
		public CommentsDTO(final CommentsEntity entity) {
			this.c_id = entity.getC_id();
			this.author = entity.getAuthor();
			this.productId = entity.getProductId();
			this.content = entity.getContent();
			this.createdDate = entity.getCreatedDate();
		}
		
		public static CommentsEntity toEntity(final CommentsDTO dto) {
			return CommentsEntity.builder()
					.c_id(dto.getC_id())
					.author(dto.getAuthor())
					.productId(dto.getProductId())
					.content(dto.getContent())
					.createdDate(dto.getCreatedDate())
					.build();
		}


}

