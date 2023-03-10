package com.samseung.ceas.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samseung.ceas.model.ProductEntity;
import com.samseung.ceas.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	public List<ProductEntity> retrieveAll() {
		List<ProductEntity> list = productRepository.findAll();
		if (list != null) {
			return productRepository.findAll();
		} else {
			log.warn("Product Table is empty");
			throw new IllegalStateException("Product Table is empty");
		}

	}

	public ProductEntity create(ProductEntity productEntity) {
		try {
			validate(productEntity);
			productRepository.save(productEntity);
			log.info("product Id: {} is saved", productEntity.getId());
			ProductEntity savedEntity = productRepository.findById(productEntity.getId()).get();
			return savedEntity;
		} catch (Exception e) {
			log.error("An error occurred while creating a product", productEntity.getId(), e);
			throw new RuntimeException("An error occurred while creating a product", e);
		}
	}

	private void validate(final ProductEntity productEntity) {
		if (productEntity == null) {
			log.warn("Entity cannot be null");
			throw new IllegalStateException("Entity cannot be null.");
		}
		if (productEntity.getUserId() == null) {
			log.warn("Unknown user");
			throw new IllegalStateException("Unknown user.");
		}
	}

	public ProductEntity retrieve(final Integer productId) {
		final Optional<ProductEntity> entity = productRepository.findById(productId);
		return entity.orElseThrow(() -> {
			log.info("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});

	}

	public ProductEntity update(final ProductEntity productEntity) {
		validate(productEntity);
		final Optional<ProductEntity> originalEntity = productRepository.findById(productEntity.getId());
		originalEntity.ifPresentOrElse((entity) -> {
			entity.setProductName(productEntity.getProductName());
			entity.setProductDescription(productEntity.getProductDescription());
			productRepository.save(entity);
		}, () -> {
			log.warn("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});
		return retrieve(productEntity.getId());

	}

	public List<ProductEntity> delete(final ProductEntity productEntity) {
		try {
			productRepository.delete(productEntity);
		} catch (Exception e) {
			log.error("An error occurred while deleting a product", productEntity.getId(), e);
			throw new RuntimeException("An error occurred while deleting a product" + productEntity.getId(), e);
		}
		return productRepository.findAll();
	}

	
	
}
