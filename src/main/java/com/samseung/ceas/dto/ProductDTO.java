package com.samseung.ceas.dto;

import java.time.LocalDateTime;

import com.samseung.ceas.model.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private Integer id;
	private String productName;
	private String category;
	private String productDescription;
	private String userId;
	private LocalDateTime createdDate;
	
	public ProductDTO(final ProductEntity entity) {
		this.id = entity.getId();
		this.productName = entity.getProductName();
		this.category = entity.getCategory();
		this.productDescription = entity.getProductDescription();
		this.userId = entity.getUserId();
		this.createdDate = entity.getCreatedDate();
	}
	
	public static ProductEntity toEntity(final ProductDTO dto) {
		return ProductEntity.builder()
				.id(dto.getId())
				.productName(dto.getProductName())
				.category(dto.getCategory())
				.productDescription(dto.getProductDescription())
				.userId(dto.getUserId())
				.createdDate(dto.getCreatedDate())
				.build();
	}
}
