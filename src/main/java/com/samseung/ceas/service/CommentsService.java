package com.samseung.ceas.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samseung.ceas.model.Comment;
import com.samseung.ceas.repository.CommentsRepository;

import lombok.extern.slf4j.Slf4j;




@Slf4j
@Service
public class CommentsService {
	
	@Autowired

	private CommentsRepository commentsRepository;
	
	public Comment create(Comment comment) {
		try { 
			validate(comment);
			commentsRepository.save(comment);
			log.info("Reply Id: {} is saved", comment.getCommentId());
			
			Comment savedEntity = commentsRepository.findById(comment.getCommentId()).get();
			return savedEntity;
		} catch (Exception e) {
			log.error("An error occurred while creating a comments", comment.getCommentId(), e);
			throw new RuntimeException("An error occurred while creating a comments", e);
		}
	}
	
	public List<Comment> retrieveAll(final Integer id) {
		List<Comment> list = commentsRepository.findByProductId(id);
		if (list != null) {
			return list;
		} else {
			log.warn("Comments Table is empty");
			throw new IllegalStateException("Comments Table is empty");
		}

	}

	private void validate(final Comment comment) {
		if (comment == null) {
			log.warn("Entity cannot be null");
			throw new IllegalStateException("Entity cannot be null.");
		}
		if (comment.getWriter() == null) {
			log.warn("Unknown user");
			throw new IllegalStateException("Unknown user.");
		}
	}
	
	public Comment retrieve(final Integer c_id) {
		final Optional<Comment> entity = commentsRepository.findById(c_id);
		return entity.orElseThrow(() -> {
			log.info("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});
	}

	public Comment update(final Comment comment) {
		validate(comment);
		final Optional<Comment> originalEntity = commentsRepository.findById(comment.getCommentId());
		originalEntity.ifPresentOrElse((entity) -> {
			
			entity.setContent(comment.getContent());
			
			commentsRepository.save(entity);
		}, () -> {
			log.warn("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});
		return retrieve(comment.getCommentId());

	}

	public List<Comment> delete(final Comment comment) {
		try {
			commentsRepository.delete(comment);
		} catch (Exception e) {
			log.error("An error occurred while deleting a comments", comment.getCommentId(), e);
			throw new RuntimeException("An error occurred while deleting a Comments" + comment.getCommentId(), e);
		}
		return commentsRepository.findAll();
	}
}
