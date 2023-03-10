package com.samseung.ceas.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samseung.ceas.dto.CommentsDTO;
import com.samseung.ceas.dto.ResponseDTO;
import com.samseung.ceas.model.CommentsEntity;
import com.samseung.ceas.service.CommentsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
@RestController
public class CommentsController {
	
	@Autowired
	private final CommentsService commentsService;

	
	@GetMapping("/{id}/comments")
	public ResponseEntity<?> retrieveCommentsList(@AuthenticationPrincipal String userId, @PathVariable("id") Integer id){
		
		try {
			
			List<CommentsEntity> entities = commentsService.retrieveAll(id);
			List<CommentsDTO> dtos = entities.stream().map(CommentsDTO::new).collect(Collectors.toList());
			
			ResponseDTO<CommentsDTO> response = ResponseDTO.<CommentsDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);			
		}catch (IllegalStateException e) {
			ResponseDTO<CommentsDTO> responseDTO = ResponseDTO.<CommentsDTO>builder().error("Product Table is empty").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}catch (Exception e) {
			ResponseDTO<CommentsDTO> response = ResponseDTO.<CommentsDTO>builder().error("An unexpected error occurred").build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> createComments (@AuthenticationPrincipal String userId, @PathVariable("id") Integer id, @RequestBody CommentsDTO dto) {
        
        try {
        	
        	CommentsEntity entity = CommentsDTO.toEntity(dto);
        	
//        	UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(RuntimeException::new);
//        	String name = userEntity.getUserName();
   
        	
        	entity.setC_id(null);
        	entity.setCreatedDate(LocalDateTime.now());
        	entity.setAuthor("!");//해결 x
        	entity.setProductId(id);
        	
            log.info("entity: " + entity.toString());
			
            CommentsEntity createdComments = commentsService.create(entity);
			List<CommentsDTO> dtos = new ArrayList<>();
			dtos.add(new CommentsDTO(createdComments));
			
			ResponseDTO<CommentsDTO> response = ResponseDTO.<CommentsDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);
		}catch (Exception e) {
			ResponseDTO<CommentsDTO> response = ResponseDTO.<CommentsDTO>builder().error("An unexpected error occurred").build();
			return ResponseEntity.badRequest().body(response);
		}
    }
    
	@PutMapping("/{id}/comments/{c_id}")
	public ResponseEntity<?> updateComments(@AuthenticationPrincipal String userId, @PathVariable("id") Integer id, @PathVariable("c_id") Integer c_id, @RequestBody CommentsDTO dto){
		try {
			
			CommentsEntity originalEntity = commentsService.retrieve(c_id);
			originalEntity.setContent(dto.getContent());
			
			CommentsEntity updatedEntity = commentsService.update(originalEntity);
			List<CommentsDTO> dtos = new ArrayList<>();
			dtos.add(new CommentsDTO(updatedEntity));
			
			ResponseDTO<CommentsDTO> response = ResponseDTO.<CommentsDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);			
		}catch (NoSuchElementException e) {
			ResponseDTO<CommentsDTO> response = ResponseDTO.<CommentsDTO>builder().error("Entity is not existed").build();
			return ResponseEntity.badRequest().body(response);
		}catch (Exception e) {
			ResponseDTO<CommentsDTO> response = ResponseDTO.<CommentsDTO>builder().error("An unexpected error occurred").build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@DeleteMapping("/{id}/comments/{c_id}")
	public ResponseEntity<?> deleteComments(@AuthenticationPrincipal String userId, @PathVariable("id") Integer id, @PathVariable("c_id") Integer c_id){
		try {
			CommentsEntity entity = commentsService.retrieve(c_id);
			commentsService.delete(entity);
			
			List<CommentsEntity> entities =  commentsService.retrieveAll(id);
			List<CommentsDTO> dtos = entities.stream().map(CommentsDTO::new).collect(Collectors.toList());
			ResponseDTO<CommentsDTO> response = ResponseDTO.<CommentsDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);
		}catch (Exception e) {
			ResponseDTO<CommentsDTO> response = ResponseDTO.<CommentsDTO>builder().error("An error occurred while deleting a Comments").build();
			return ResponseEntity.badRequest().body(response);
		}
	}
}
