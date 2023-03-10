package com.samseung.ceas.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samseung.ceas.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	Optional<Product> findById(Integer productId);
	
	Page<Product> findAll(Pageable pageable);
	
	
}