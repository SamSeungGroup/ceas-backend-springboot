package com.samseung.ceas.dto;

import com.samseung.ceas.model.ImageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
	
	public class ImageDTO {
	private long id;
	private String storedFileName;
		

		
		public ImageDTO(final ImageEntity entity) {
			this.id = entity.getId();
			this.storedFileName = entity.getStoredFileName();
		}
		
		public static ImageEntity toEntity(final ImageDTO dto) {
			return ImageEntity.builder()
					.id(dto.getId())
					.storedFileName(dto.getStoredFileName())
					.build();
		}
}


