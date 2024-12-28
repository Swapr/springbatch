package com.example.SpringBatch.repository;

import java.util.Optional;

import javax.print.attribute.standard.JobName;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.SpringBatch.config.JsonBatchConfig;



@Service
public class JsonBatchService {
	
	private JsonBatchConfig jsonBatchConfig;
	private Job jsonImportJob;
	private JobLauncher jobLauncher;
	private Long jobexecutionId;
	private JobExplorer jobExplorer;
	@Autowired
	  public JsonBatchService(JsonBatchConfig jsonBatchConfig,@Qualifier("jsonImportJob") Job jsonImportJob,JobLauncher jobLauncher,JobExplorer jobExplorer) {
		this.jsonBatchConfig=jsonBatchConfig;
		this.jsonImportJob=jsonImportJob;
		this.jobLauncher=jobLauncher;
		this.jobExplorer=jobExplorer;
	}
	

	public Optional<JobExecution>  startJsonJob() {
		
		JobParameters jobParameter= new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
				                                              .toJobParameters();                                                 
		
		try {
			JobExecution jobExecution= jobLauncher.run(jsonImportJob, jobParameter);
			jobexecutionId= jobExecution.getJobId();
			return Optional.of(jobExecution);			
		} 
		catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			return Optional.ofNullable(null);	
		}
	}
	
	public Optional<BatchStatus>  getJsonImportJobStatus() {
		if( jobexecutionId!=null ) {
			JobExecution jobExecution=jobExplorer.getJobExecution(jobexecutionId);
			return Optional.of(jobExecution.getStatus());
		}
		else
			return Optional.ofNullable(null);
		
	}
	
	
	
	
}
