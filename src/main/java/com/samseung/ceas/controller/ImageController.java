package com.samseung.ceas.controller;

import com.samseung.ceas.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.samseung.ceas.model.Image;
import com.samseung.ceas.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getImage (@PathVariable("userId") String userId){
        try{
            Image image = imageService.findByUserId(userId);
            UrlResource resource = new UrlResource("file:" + image.getStoredFileName());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
        }catch (Exception e){
            ResponseDto response = ResponseDto.builder().error(e.getMessage()).build();
            return  ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getImage (@PathVariable("productId") long productId) {
        try {
            Image image = imageService.findByProductId(productId);
            UrlResource resource = new UrlResource("file:" + image.getStoredFileName());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
        }catch (Exception e){
            ResponseDto response = ResponseDto.builder().error(e.getMessage()).build();
            return  ResponseEntity.badRequest().body(response);
        }
    }
}

