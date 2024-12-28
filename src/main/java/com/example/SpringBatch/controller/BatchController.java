package com.example.SpringBatch.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringBatch.exceptioon.IncorrectCSVFileFormatException;
import com.example.SpringBatch.service.KafkaProducerService;

import ch.qos.logback.classic.Logger;


@RestController
@RequestMapping("/batch")
public class BatchController {
	
	
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;
	@Autowired
	@Qualifier("asyncJobLauncher")
	private JobLauncher asyncJobLauncher;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Autowired 
	private KafkaProducerService kafkaProducerService;
	
	private Long jobExecutionId;
	
	public static final Logger logger= (Logger) LoggerFactory.getLogger(BatchController.class);
	
	private List<Throwable> jobExceptionsRaised = new ArrayList<>();
	
	
	@RequestMapping("/startbatch")
	public String startbatch()
	{
		JobParameters jobParameters=new JobParametersBuilder()
				      .addLong("timestamp", System.currentTimeMillis())
				      .toJobParameters();
				        
		try {
			 JobExecution jobExecution =jobLauncher.run(job, jobParameters);          
			 
			  BatchStatus batchStatus=jobExecution.getStatus();
			  
			 if(batchStatus==BatchStatus.COMPLETED)
			 {
				 return "batch completed";
			 }
			 
			 else
			 {
				 for (Throwable exception: jobExecution.getAllFailureExceptions())
				 {
					 if ( exception instanceof FlatFileParseException )
					 {
						 throw new IncorrectCSVFileFormatException("csv file format incorrect");
					 }
					 else
						 return "batch failed";
				 }
				 
				 return "batch failed";
			 }
			 
			 
			 
					 
			 	 
		}catch (FlatFileFormatException e) {
			throw new IncorrectCSVFileFormatException("please check CSV file format ");
			
		}catch (FlatFileParseException e) {
		    throw new IncorrectCSVFileFormatException("please check csv file format");
		} catch (JobExecutionAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobRestartException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "request completed but batch not started";
	}
	
	
	
	@RequestMapping("/startbatch/getstatus")
	   public String startbatchwithstatus() {
		
		JobParameters jobParameters=new JobParametersBuilder()
			      .addLong("timestamp", System.currentTimeMillis())
			      .toJobParameters();

		
		try {
		 JobExecution jobExecution =asyncJobLauncher.run(job, jobParameters); 
		 logger.info("launcher started job");
		 kafkaProducerService.sendMessage("topic1", "message to kafka : job initiated current job status is:"+jobExecution.getStatus().toString());
		 
		 try {
			  BatchStatus batchStatus=jobExecution.getStatus();
			  System.out.println("job launched and status is "+batchStatus.toString());    //batch status is starting 
			Thread.sleep(4000);                                                           //waitinig for batch status to change from starting to start
	         
			
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		 
		 if(!jobExecution.getAllFailureExceptions().isEmpty())
		 {
			 for(Throwable exception:jobExecution.getAllFailureExceptions())
			 {
				 jobExceptionsRaised.add(exception);
			 }
		 }
		 System.out.println("Size of jbexceptionraised"+jobExceptionsRaised.size());
		               jobExecutionId =jobExecution.getJobId();
		 
			 
			  BatchStatus batchStatus=jobExecution.getStatus();
			  System.out.println("completed thread sleep now job status is  "+batchStatus.toString());
	     
			  
		  
		 if( batchStatus==BatchStatus.STARTED)
		 {
			 kafkaProducerService.sendMessage("topic1","job has started ");
			  return "batch started";
		 }
		 else
		 {
			 
		 }
		    kafkaProducerService.sendMessage("topic1", "job  failed");
			 return "batch failed";
		
		 
			 
		 
		 		 	 
	}catch (FlatFileFormatException e) {
		throw new IncorrectCSVFileFormatException("please check CSV file format ");
		
	}catch (FlatFileParseException e) {
	    throw new IncorrectCSVFileFormatException("please check csv file format");
	} catch (JobExecutionAlreadyRunningException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JobRestartException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JobInstanceAlreadyCompleteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JobParametersInvalidException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
		return "batch failed";
	}
	
	
	
	
	
	@RequestMapping("/getbatchstatus")
	public ResponseEntity<String> getBatchStatus() {
		
		if(jobExecutionId!=null) {
			
			JobExecution jobExecution=jobExplorer.getJobExecution(jobExecutionId);
			
			/*
			 * List<Throwable> failureExceptions=jobExecution.getAllFailureExceptions();
			 * System.out.println(":size of failure exceptions"+ failureExceptions.size());
			 */
			System.out.println("Size of jbexceptionraised"+jobExceptionsRaised.size());
			
			 for (Throwable exception: jobExceptionsRaised)
			 {
				 if ( exception instanceof FlatFileParseException )
				 {
					 kafkaProducerService.sendMessage("topic1", "job failed due to incorrect csv file eformat");
					 throw new IncorrectCSVFileFormatException("batch failed due to input csv file format incorrect");
					 
				 }
				 
			 }

			 kafkaProducerService.sendMessage("topic1", "job status is "+jobExecution.getStatus().toString());
			
			return ResponseEntity.ok(jobExecution.getStatus().toString());
		}
		kafkaProducerService.sendMessage("topic1", "job not stared please start job first");
		return ResponseEntity.badRequest().body("batch not started please start batch first");
		
	}

}
