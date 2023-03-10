
package com.samseung.ceas.dto;

import java.time.LocalDateTime;

import com.samseung.ceas.model.Product;

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
	private String productDescription;
	private String userId;
	private LocalDateTime createdDate;
	
	public ProductDTO(final Product entity) {
		this.id = entity.getId();
		this.productName = entity.getProductName();
		this.productDescription = entity.getProductDescription();
		this.userId = entity.getUserId();
		this.createdDate = entity.getCreatedDate();
	}
	
	public static Product toEntity(final ProductDTO dto) {
		return Product.builder()
				.id(dto.getId())
				.productName(dto.getProductName())
				.productDescription(dto.getProductDescription())
				.userId(dto.getUserId())
				.createdDate(dto.getCreatedDate())
				.build();
	}
}
