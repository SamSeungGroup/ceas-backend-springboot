package com.samseung.ceas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samseung.ceas.dto.ProductDTO;
import com.samseung.ceas.model.ProductEntity;
import com.samseung.ceas.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PageController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/page")
    public Page<ProductDTO> paging(@AuthenticationPrincipal String userId, Model model, @PageableDefault(size=10, sort="createdDate") Pageable pageRequest) {
 
        Page<ProductEntity> productList = productRepository.findAll(pageRequest);

        Page<ProductDTO> pagingList = productList.map(
                post -> new ProductDTO(post.getId(),post.getProductName(), post.getProductDescription(), post.getUserId(), post.getCreatedDate()));
        
        //html로 넘기는 함수
        model.addAttribute("pagingList", pagingList);
        return pagingList;
    }
}