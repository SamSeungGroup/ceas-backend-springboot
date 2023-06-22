package com.samseung.ceas.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samseung.ceas.model.Product;
import com.samseung.ceas.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	public List<Product> retrieveAll() {
		List<Product> list = productRepository.findAll();
		if (list != null) {
			return productRepository.findAll();
		} else {
			log.warn("Product Table is empty");
			throw new IllegalStateException("Product Table is empty");
		}
	}

	public Product create(Product product) {
		try {
			validate(product);
			Product savedEntity = productRepository.save(product);
			log.info("product Id: {} is saved", savedEntity.getId());
			return savedEntity;
		} catch (Exception e) {
			log.error("An error occurred while creating a product", product.getId(), e);
			throw new RuntimeException("An error occurred while creating a product", e);
		}
	}

	private void validate(final Product product) {
		if (product == null) {
			log.warn("Entity cannot be null");
			throw new IllegalStateException("Entity cannot be null.");
		}
		if (product.getSeller() == null) {
			log.warn("Unknown user");
			throw new IllegalStateException("Unknown user.");
		}
	}

	public Product retrieve(final Long productId) {
		final Optional<Product> entity = productRepository.findById(productId);
		return entity.orElseThrow(() -> {
			log.info("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});

	}

	public Product update(final Product product) {
		validate(product);
		final Optional<Product> originalEntity = productRepository.findById(product.getId());
		originalEntity.ifPresentOrElse((entity) -> {
			entity.setProductName(product.getProductName());
			entity.setProductPrice(product.getProductPrice());
			entity.setProductImage(product.getProductImage());
			entity.setProductDescription(product.getProductDescription());
			entity.setProductPositive(product.getProductPositive());
			productRepository.save(entity);
			log.info("Product Id: {} is updated", entity.getId());
		}, () -> {
			log.warn("Entity is not existed");
			throw new NoSuchElementException("Entity is not existed");
		});
		return retrieve(product.getId());
	}

	public List<Product> delete(final Product product) {
		try {
			productRepository.delete(product);
			log.info("Product Id: {} is deleted", product.getId());
		} catch (Exception e) {
			log.error("An error occurred while deleting a product", product.getId(), e);
			throw new RuntimeException("An error occurred while deleting a product" + product.getId(), e);
		}
		return productRepository.findAll();
	}
}
