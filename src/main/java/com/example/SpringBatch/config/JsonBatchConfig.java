package com.example.SpringBatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;
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
      
      
      @Bean
      public Step jsonJobStep1(JobRepository jobRepository,JpaTransactionManager jpaTransactionManager,JsonItemReader< User1> jsonItemReader,JpaItemWriter<User1> jpaItemWriter) {
    	  
    	  return new StepBuilder("jsonJobStep1",jobRepository).<User1,User1>chunk(2,jpaTransactionManager)
    	                                               .reader(jsonItemReader)
    	                                               .writer(jpaItemWriter)
    	                                               .build();
    	  
      }
      
      
      @Bean
      public Job jsonImportJob(JobRepository jobRepository,Step jsonJobStep1) {
    	   return   new JobBuilder("jsonImportJob",jobRepository)
    	                   .start(jsonJobStep1)
    	                   .build();
    	  
      }
      
	     

}
