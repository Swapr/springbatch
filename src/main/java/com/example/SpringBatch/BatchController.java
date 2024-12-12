package com.example.SpringBatch;

import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringBatch.exceptioon.IncorrectCSVFileFormatException;

@RestController
@RequestMapping("/batch")
public class BatchController {
	
	
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;
	
	
	@RequestMapping("/startbatch")
	public String startbatch()
	{
		JobParameters jobParameters=new JobParametersBuilder()
				      .addLong("timestamp", System.currentTimeMillis())
				      .toJobParameters();
				        
		try {
			 JobExecution jobExecution =jobLauncher.run(job, jobParameters);          
			 try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
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

}
