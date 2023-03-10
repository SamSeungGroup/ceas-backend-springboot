package com.samseung.ceas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samseung.ceas.model.CommentsEntity;
import com.samseung.ceas.model.ProductEntity;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsEntity, Integer>{

	Optional<CommentsEntity> findById(Integer c_id);
	List<CommentsEntity> findByProductId(Integer productId);


}

