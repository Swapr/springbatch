package com.example.SpringBatch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//this class is for json file to be store in databse usinig spring batch

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User1 {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer userId;
	private String firstName;
	private String lastName;
	private String country;
	private String email;


}
