package com.samseung.ceas.controller;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.samseung.ceas.dto.CommentsDTO;
import com.samseung.ceas.dto.ImageDTO;
import com.samseung.ceas.dto.ProductDTO;
import com.samseung.ceas.dto.ResponseDTO;
import com.samseung.ceas.model.CommentsEntity;
import com.samseung.ceas.model.ImageEntity;
import com.samseung.ceas.model.ProductEntity;
import com.samseung.ceas.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageController {
	
    private final ImageService imageService;

    @PostMapping("/image")
    public ResponseEntity<?> createImage(@AuthenticationPrincipal String userId, @Validated @RequestParam("image") List<MultipartFile> files) throws Exception {
    	imageService.addImage(ImageEntity.builder()
                .build(), files);
    	
    	
        return ResponseEntity.ok().build();
    }
    
//    @ResponseBody
//    @GetMapping("/image/{id}")
//    public UrlResource getImage(@AuthenticationPrincipal String userId, @PathVariable("id") long id)throws MalformedURLException {
//    	
//    	ImageEntity imageEntity = imageService.findImage(id).orElseThrow(RuntimeException::new);
//    	
//    	
//        String imgPath = imageEntity.getStoredFileName();
//        
//        return new UrlResource("file:" + imgPath);
//    }
    
//    @GetMapping("/image/{id}")
//    public ResponseEntity<?> getImage (@AuthenticationPrincipal String userId, @PathVariable("id") long id)throws MalformedURLException {
//        
//        try {
//        	
//        	ImageEntity imageEntity = imageService.findImage(id).get();
//        	
//           	
//        	ImageDTO imageDTO = new ImageDTO();
//        	imageDTO.setId(imageEntity.getId());
//        	imageDTO.setStoredFileName(imageEntity.getStoredFileName());
//        	
//        	List<ImageDTO> dtos = imageEntity.stream().map(ImageDTO::new).collect(Collectors.toList());
//			
//			ResponseDTO<ImageDTO> response = ResponseDTO.<ImageDTO>builder().data(dtos).build();
//			return ResponseEntity.ok().body(response);			
//		}catch (NoSuchElementException e) {
//			ResponseDTO<ImageDTO> response = ResponseDTO.<ImageDTO>builder().error("Entity is not existed").build();
//			return ResponseEntity.badRequest().body(response);
//		}catch (Exception e) {
//			ResponseDTO<ImageDTO> response = ResponseDTO.<ImageDTO>builder().error("An unexpected error occurred").build();
//			return ResponseEntity.badRequest().body(response);
//		}
//    }
    
}