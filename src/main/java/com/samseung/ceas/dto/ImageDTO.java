package com.samseung.ceas.dto;

import com.samseung.ceas.model.Image;

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
		

		
		public ImageDTO(final Image entity) {
			this.id = entity.getId();
			this.storedFileName = entity.getStoredFileName();
		}
		
		public static Image toEntity(final ImageDTO dto) {
			return Image.builder()
					.id(dto.getId())
					.storedFileName(dto.getStoredFileName())
					.build();
		}
}


