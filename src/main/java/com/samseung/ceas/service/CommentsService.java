package com.samseung.ceas.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samseung.ceas.model.CommentsEntity;
import com.samseung.ceas.repository.CommentsRepository;

import lombok.extern.slf4j.Slf4j;




@Slf4j
@Service
public class CommentsService {
	
	@Autowired

	private CommentsRepository commentsRepository;
	
	public CommentsEntity create(CommentsEntity commentsEntity) {
		try { 
			validate(commentsEntity);
			commentsRepository.save(commentsEntity);
			log.info("Reply Id: {} is saved", commentsEntity.getC_id());
			
			CommentsEntity savedEntity = commentsRepository.findById(commentsEntity.getC_id()).get();
			return savedEntity;
		} catch (Exception e) {
			log.error("An error occurred while creating a comments", commentsEntity.getC_id(), e);
			throw new RuntimeException("An error occurred while creating a comments", e);
		}
	}
	
	public List<CommentsEntity> retrieveAll(final Integer id) {
		List<CommentsEntity> list = commentsRepository.findByProductId(id);
		if (list != null) {
			return list;
		} else {
			log.warn("Comments Table is empty");
			throw new IllegalStateException("Comments Table is empty");
		}

	}

	private void validate(final CommentsEntity commentsEntity) {
		if (commentsEntity == null) {
			log.warn("Entity cannot be null");
			throw new IllegalStateException("Entity cannot be null.");
		}
		if (commentsEntity.getAuthor() == null) {
			log.warn("Unknown user");
			throw new IllegalStateException("Unknown user.");
		}
	}
	
	public CommentsEntity retrieve(final Integer c_id) {
		final Optional<CommentsEntity> entity = commentsRepository.findById(c_id);
		return entity.orElseThrow(() -> {
			log.info("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});
	}

	public CommentsEntity update(final CommentsEntity commentsEntity) {
		validate(commentsEntity);
		final Optional<CommentsEntity> originalEntity = commentsRepository.findById(commentsEntity.getC_id());
		originalEntity.ifPresentOrElse((entity) -> {
			
			entity.setContent(commentsEntity.getContent());
			
			commentsRepository.save(entity);
		}, () -> {
			log.warn("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});
		return retrieve(commentsEntity.getC_id());

	}

	public List<CommentsEntity> delete(final CommentsEntity commentsEntity) {
		try {
			commentsRepository.delete(commentsEntity);
		} catch (Exception e) {
			log.error("An error occurred while deleting a comments", commentsEntity.getC_id(), e);
			throw new RuntimeException("An error occurred while deleting a Comments" + commentsEntity.getC_id(), e);
		}
		return commentsRepository.findAll();
	}
}
