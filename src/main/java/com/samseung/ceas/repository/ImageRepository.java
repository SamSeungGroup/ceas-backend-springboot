package com.samseung.ceas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.samseung.ceas.model.Image;

@Repository 
public interface  ImageRepository extends JpaRepository<Image, Long> {
    Image findByUser_Id(String userId);
    Image findByProduct_Id(Long productId);
}