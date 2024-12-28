package com.example.SpringBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SpringBatch.model.User1;

@Repository
public interface User1Repository extends JpaRepository<User1,Integer>{
	
	
	

}
