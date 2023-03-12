package com.samseung.ceas.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.samseung.ceas.model.Comment;
import com.samseung.ceas.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	
	public Comment create(Comment comment) {
		try { 
			validate(comment);
			commentRepository.save(comment);
			log.info("Reply Id: {} is saved", comment.getId());
			
			Comment savedEntity = commentRepository.findById(comment.getId()).get();
			return savedEntity;
		} catch (Exception e) {
			log.error("An error occurred while creating a comments", comment.getId(), e);
			throw new RuntimeException("An error occurred while creating a comments", e);
		}
	}
	
	public List<Comment> retrieveAll(final Long id) {
		List<Comment> list = commentRepository.findByProductId(id);
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
	
	public Comment retrieve(final Long id) {
		final Optional<Comment> entity = commentRepository.findById(id);
		return entity.orElseThrow(() -> {
			log.info("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});
	}

	public Comment update(final Comment comment) {
		validate(comment);
		final Optional<Comment> originalEntity = commentRepository.findById(comment.getId());
		originalEntity.ifPresentOrElse((entity) -> {
			
			entity.setContent(comment.getContent());
			
			commentRepository.save(entity);
		}, () -> {
			log.warn("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});
		return retrieve(comment.getId());

	}

	public List<Comment> delete(final Comment comment) {
		try {
			commentRepository.delete(comment);
		} catch (Exception e) {
			log.error("An error occurred while deleting a comments", comment.getId(), e);
			throw new RuntimeException("An error occurred while deleting a Comments" + comment.getId(), e);
		}
		return commentRepository.findAll();
	}
}
