package com.example.SpringBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SpringBatch.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	

}
