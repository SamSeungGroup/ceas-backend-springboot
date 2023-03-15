package com.samseung.ceas.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.samseung.ceas.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.samseung.ceas.dto.ProductDTO;
import com.samseung.ceas.dto.ResponseDtos;
import com.samseung.ceas.model.Image;
import com.samseung.ceas.model.Product;
import com.samseung.ceas.service.ImageService;
import com.samseung.ceas.service.ProductService;
import com.samseung.ceas.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<?> retrieveProductList() {
        try {
            List<Product> products = productService.retrieveAll();
            List<ProductDTO> dtos = products.stream().map(ProductDTO::new).collect(Collectors.toList());
            ResponseDtos<ProductDTO> response = ResponseDtos.<ProductDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (IllegalStateException e) {
            ResponseDtos<ProductDTO> responseDtos = ResponseDtos.<ProductDTO>builder().error("Product Table is empty")
                    .build();
            return ResponseEntity.badRequest().body(responseDtos);
        } catch (Exception e) {
            ResponseDtos<ProductDTO> response = ResponseDtos.<ProductDTO>builder().error("An unexpected error occurred")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@AuthenticationPrincipal String userId,
                                           @RequestPart("dto") ProductDTO dto,
                                           @Validated @RequestPart("image") List<MultipartFile> files) {
        try {
            Product product = ProductDTO.toEntity(dto);

            product.setId(null);
            product.setSeller(userService.retrieve(userId));
            product.setCreatedDate(LocalDateTime.now());

            Product createdProduct = productService.create(product);

            ProductDTO productDto = new ProductDTO(createdProduct);
            Image image = Image.builder().build();
            imageService.addProductImage(image, files, productDto);
            Image savedImage = imageService.findByProductId(productDto.getId());
            createdProduct.setProductImage(savedImage);

            Product updatedProduct = productService.update(createdProduct);

            productDto = new ProductDTO(updatedProduct);
            ResponseDto<ProductDTO> response = ResponseDto.<ProductDTO>builder().data(productDto).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDtos<ProductDTO> response = ResponseDtos.<ProductDTO>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveProduct(@PathVariable("id") Long id) {
        try {
            Product product = productService.retrieve(id);
            ProductDTO dto = new ProductDTO(product);

            ResponseDto<ProductDTO> response = ResponseDto.<ProductDTO>builder().data(dto).build();
            return ResponseEntity.ok().body(response);
        } catch (NoSuchElementException e) {
            ResponseDto<ProductDTO> response = ResponseDto.<ProductDTO>builder().error("Entity is not existed").build();
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseDto<ProductDTO> response = ResponseDto.<ProductDTO>builder().error("An unexpected error occurred").build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@AuthenticationPrincipal String userId,
                                           @PathVariable("id") Long id,
                                           @RequestPart("dto") ProductDTO dto,
                                           @Validated @RequestPart("image") List<MultipartFile> files) {
        try {
            Product product = productService.retrieve(id);

            Image image = imageService.findByProductId(id);
            imageService.addProductImage(image, files, dto);
            Image savedImage = imageService.findByProductId(product.getId());

            product.setProductName(dto.getProductName());
            product.setProductDescription(dto.getProductDescription());
            product.setProductPrice(dto.getProductPrice());
            product.setProductImage(savedImage);

            Product updatedProduct = productService.update(product);
            ProductDTO productDto = new ProductDTO(updatedProduct);

            ResponseDto<ProductDTO> response = ResponseDto.<ProductDTO>builder().data(productDto).build();
            return ResponseEntity.ok().body(response);
        } catch (NoSuchElementException e) {
            ResponseDto<ProductDTO> response = ResponseDto.<ProductDTO>builder().error("Entity is not existed").build();
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseDto<ProductDTO> response = ResponseDto.<ProductDTO>builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal String userId, @PathVariable("id") Long id) {
        try {
            Product product = productService.retrieve(id);
            productService.delete(product);
            File file = new File(product.getProductImage().getStoredFileName());
            file.delete();

            List<Product> products = productService.retrieveAll();
            List<ProductDTO> dtos = products.stream().map(ProductDTO::new).collect(Collectors.toList());
            ResponseDtos<ProductDTO> response = ResponseDtos.<ProductDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDto<ProductDTO> response = ResponseDto.<ProductDTO>builder().error("An error occurred while deleting a product").build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
