package com.samseung.ceas.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.samseung.ceas.dto.ResponseDto;
import com.samseung.ceas.service.ProductService;
import com.samseung.ceas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.samseung.ceas.dto.CommentDTO;
import com.samseung.ceas.dto.ResponseDtos;
import com.samseung.ceas.model.Comment;
import com.samseung.ceas.service.CommentService;

@RequestMapping("/products")
@RestController
public class CommentController {
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/{product_id}/comments")
    public ResponseEntity<?> retrieveCommentsList(@AuthenticationPrincipal String userId, @PathVariable("product_id") Long productId) {
        try {
            List<Comment> entities = commentService.retrieveAll(productId);
            List<CommentDTO> dtos = entities.stream().map(CommentDTO::new).collect(Collectors.toList());

            ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (IllegalStateException e) {
            ResponseDto<CommentDTO> responseDtos = ResponseDto.<CommentDTO>builder().error("Product Table is empty").build();
            return ResponseEntity.badRequest().body(responseDtos);
        } catch (Exception e) {
            ResponseDto<CommentDTO> response = ResponseDto.<CommentDTO>builder().error("An unexpected error occurred").build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{product_id}/comments")
    public ResponseEntity<?> createComments(@AuthenticationPrincipal String userId, @PathVariable("product_id") Long productId, @RequestBody CommentDTO dto) {
        try {
            Comment comment = CommentDTO.toEntity(dto);

			comment.setId(null);
			comment.setCreatedDate(LocalDateTime.now());
			comment.setWriter(userService.retrieve(userId));
			comment.setProduct(productService.retrieve(productId));

            Comment createdComment = commentService.create(comment);

            CommentDTO commentDto = new CommentDTO(createdComment);
            ResponseDto<CommentDTO> response = ResponseDto.<CommentDTO>builder().data(commentDto).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDto<CommentDTO> response = ResponseDto.<CommentDTO>builder().error("An unexpected error occurred").build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{product_id}/comments/{id}")
    public ResponseEntity<?> updateComments(@AuthenticationPrincipal String userId, @PathVariable("product_id") Long productId, @PathVariable("id") Long id, @RequestBody CommentDTO dto) {
        try {
            Comment origin = commentService.retrieve(id);
			origin.setContent(dto.getContent());

            Comment updatedComment = commentService.update(origin);

			CommentDTO commentDto = new CommentDTO(updatedComment);
            ResponseDto<CommentDTO> response = ResponseDto.<CommentDTO>builder().data(commentDto).build();
            return ResponseEntity.ok().body(response);
        } catch (NoSuchElementException e) {
            ResponseDto<CommentDTO> response = ResponseDto.<CommentDTO>builder().error("Entity is not existed").build();
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseDto<CommentDTO> response = ResponseDto.<CommentDTO>builder().error("An unexpected error occurred").build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{product_id}/comments/{id}")
    public ResponseEntity<?> deleteComments(@AuthenticationPrincipal String userId, @PathVariable("product_id") Long productId, @PathVariable("id") Long id) {
        try {
            Comment coment = commentService.retrieve(id);
            commentService.delete(coment);

            List<Comment> comments = commentService.retrieveAll(productId);
            List<CommentDTO> dtos = comments.stream().map(CommentDTO::new).collect(Collectors.toList());
            ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDtos<CommentDTO> response = ResponseDtos.<CommentDTO>builder().error("An error occurred while deleting a Comments").build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
