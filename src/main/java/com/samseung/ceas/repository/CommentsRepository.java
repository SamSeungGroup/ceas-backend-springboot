package com.samseung.ceas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samseung.ceas.model.Comment;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Integer>{

	Optional<Comment> findById(Integer c_id);
	List<Comment> findByProductId(Integer productId);


}

