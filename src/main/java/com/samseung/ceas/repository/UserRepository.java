package com.samseung.ceas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samseung.ceas.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	User findByUserId(String userId);
	Boolean existsByUserId(String userId);
	User findByUserIdAndUserPassword(String userId, String userPassword);
	
	
}
