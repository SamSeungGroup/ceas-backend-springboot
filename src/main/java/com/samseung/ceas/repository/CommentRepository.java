package com.samseung.ceas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samseung.ceas.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByProductId(Long productId);
}

