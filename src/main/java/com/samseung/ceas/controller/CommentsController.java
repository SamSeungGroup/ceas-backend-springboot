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

import com.samseung.ceas.dto.CommentDTO;
import com.samseung.ceas.dto.ResponseDtos;
import com.samseung.ceas.model.Comment;
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
			
			List<Comment> entities = commentsService.retrieveAll(id);
			List<CommentDTO> dtos = entities.stream().map(CommentDTO::new).collect(Collectors.toList());
			
			ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);			
		}catch (IllegalStateException e) {
			ResponseDtos<CommentDTO> responseDtos = ResponseDtos.<CommentDTO>builder().error("Product Table is empty").build();
			return ResponseEntity.badRequest().body(responseDtos);
		}catch (Exception e) {
			ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().error("An unexpected error occurred").build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
//    @PostMapping("/{id}/comments")
//    public ResponseEntity<?> createComments (@AuthenticationPrincipal String userId, @PathVariable("id") Integer id, @RequestBody CommentDTO dto) {
//        
//        try {
//        	
//        	Comment entity = CommentDTO.toEntity(dto);
//        	
////        	UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(RuntimeException::new);
////        	String name = userEntity.getUserName();
//   
//        	
//        	entity.setC_id(null);
//        	entity.setCreatedDate(LocalDateTime.now());
//        	entity.setAuthor("!");//해결 x
//        	entity.setProductId(id);
//        	
//            log.info("entity: " + entity.toString());
//			
//            Comment createdComments = commentsService.create(entity);
//			List<CommentDTO> dtos = new ArrayList<>();
//			dtos.add(new CommentDTO(createdComments));
//			
//			ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().data(dtos).build();
//			return ResponseEntity.ok().body(response);
//		}catch (Exception e) {
//			ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().error("An unexpected error occurred").build();
//			return ResponseEntity.badRequest().body(response);
//		}
//    }
    
	@PutMapping("/{id}/comments/{c_id}")
	public ResponseEntity<?> updateComments(@AuthenticationPrincipal String userId, @PathVariable("id") Integer id, @PathVariable("c_id") Integer c_id, @RequestBody CommentDTO dto){
		try {
			
			Comment originalEntity = commentsService.retrieve(c_id);
			originalEntity.setContent(dto.getContent());
			
			Comment updatedEntity = commentsService.update(originalEntity);
			List<CommentDTO> dtos = new ArrayList<>();
			dtos.add(new CommentDTO(updatedEntity));
			
			ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);			
		}catch (NoSuchElementException e) {
			ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().error("Entity is not existed").build();
			return ResponseEntity.badRequest().body(response);
		}catch (Exception e) {
			ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().error("An unexpected error occurred").build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@DeleteMapping("/{id}/comments/{c_id}")
	public ResponseEntity<?> deleteComments(@AuthenticationPrincipal String userId, @PathVariable("id") Integer id, @PathVariable("c_id") Integer c_id){
		try {
			Comment entity = commentsService.retrieve(c_id);
			commentsService.delete(entity);
			
			List<Comment> entities =  commentsService.retrieveAll(id);
			List<CommentDTO> dtos = entities.stream().map(CommentDTO::new).collect(Collectors.toList());
			ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().data(dtos).build();
			return ResponseEntity.ok().body(response);
		}catch (Exception e) {
			ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().error("An error occurred while deleting a Comments").build();
			return ResponseEntity.badRequest().body(response);
		}
	}
}
