package com.samseung.ceas.controller;

import com.samseung.ceas.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.samseung.ceas.model.Image;
import com.samseung.ceas.service.ImageService;

@Slf4j
@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getImage (@PathVariable("userId") String userId){
        try{
            Image image = imageService.findByUserId(userId);
            String imgUrl;
            if(image == null){
                imgUrl = "file:src/main/resources/static/image/default.jpg";
            }else{
                imgUrl = "file:" + image.getStoredFileName();
            }
            log.info(imgUrl);
            UrlResource resource = new UrlResource(imgUrl);
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

