package com.samseung.ceas.controller;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.samseung.ceas.dto.ResponseDtos;
import com.samseung.ceas.model.Image;
import com.samseung.ceas.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageController {
	
    private final ImageService imageService;

    @PostMapping("/image")
    public ResponseEntity<?> createImage(@AuthenticationPrincipal String userId, @Validated @RequestParam("image") List<MultipartFile> files) throws Exception {
    	imageService.addImage(Image.builder()
                .build(), files);
    	
    	
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImage (@AuthenticationPrincipal String userId, @PathVariable("id") long id)throws MalformedURLException {
        
        try {
        	
        	Image image = imageService.findImage(id).get();
        	
           	
//        	ImageDTO imageDTO = new ImageDTO();
//        	imageDTO.setId(imageEntity.getId());
//        	imageDTO.setStoredFileName(imageEntity.getStoredFileName());
        	
			List<ImageDTO> dtos = new ArrayList<>();
			dtos.add(new ImageDTO(image));
        				
			ResponseDtos<ImageDTO> response = ResponseDtos.<ImageDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);			
		}catch (NoSuchElementException e) {
			ResponseDtos<ImageDTO> response = ResponseDtos.<ImageDTO>builder().error("Entity is not existed").build();
			return ResponseEntity.badRequest().body(response);
		}catch (Exception e) {
			ResponseDtos<ImageDTO> response = ResponseDtos.<ImageDTO>builder().error("An unexpected error occurred").build();
			return ResponseEntity.badRequest().body(response);
		}
    }
    
}