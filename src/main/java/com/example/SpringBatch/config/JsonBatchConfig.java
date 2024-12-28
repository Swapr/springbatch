package com.example.SpringBatch.config;

import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.batch.item.json.JsonItemReader;
import com.example.SpringBatch.model.User1;

import jakarta.persistence.EntityManagerFactory;


@Configuration
public class JsonBatchConfig {
	
	  @Value("${user1jsonfile}")	
	     private String resourceLocation;
	  
	  @Autowired
	  private EntityManagerFactory entityManagerFactory;
      
      @Bean
	  public  JsonItemReader<User1> jsonItemReader() {
	    	 
	    	return  new JsonItemReaderBuilder<User1>().jsonObjectReader(new JacksonJsonObjectReader<>(User1.class))
	    	                                   .resource(new FileSystemResource(resourceLocation))
	    	                                   .name("jsonItemreader")
	    	                                   .build();
	     }
      
      @Bean
      public JpaItemWriter<User1> jpaItemWriter(){
    	  
    	  JpaItemWriter<User1> jpaItemWriter=new JpaItemWriter<>();
    	  jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    	  return jpaItemWriter;
    	  
      }
	     

}
