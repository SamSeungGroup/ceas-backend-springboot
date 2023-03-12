
package com.samseung.ceas.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.samseung.ceas.model.Comment;
import com.samseung.ceas.model.Image;
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
public class ProductDTO {
	private Long id;
	private String productName;
	private String productDescription;
	private Image productImage;
	private Integer productPrice;
	private Double productPositive;
	private List<Comment> commentList;
	private User seller;
	private LocalDateTime createdDate;
	
	public ProductDTO(final Product product) {
		this.id = product.getId();
		this.productName = product.getProductName();
		this.productDescription = product.getProductDescription();
		this.productImage = product.getProductImage();
		this.productPrice = product.getProductPrice();
		this.productPositive = product.getProductPositive();
		this.commentList = product.getCommentList();
		this.seller = product.getSeller();
		this.createdDate = product.getCreatedDate();
	}
	
	public static Product toEntity(final ProductDTO dto) {
		return Product.builder()
				.id(dto.getId())
				.productName(dto.getProductName())
				.productDescription(dto.getProductDescription())
				.productPrice(dto.getProductPrice())
				.productPositive(dto.getProductPositive())
				.commentList(dto.getCommentList())
				.seller(dto.getSeller())
				.createdDate(dto.getCreatedDate())
				.build();
	}
}
