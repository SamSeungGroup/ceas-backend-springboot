package com.samseung.ceas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.samseung.ceas.model.ImageEntity;



@Repository 
public interface  ImageRepository extends JpaRepository<ImageEntity, Long> {


}