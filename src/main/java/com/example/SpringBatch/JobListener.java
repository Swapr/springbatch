package com.example.SpringBatch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobListener implements JobExecutionListener {
	
    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Handle job start logic
        System.out.println("Job started");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchStatus status = jobExecution.getStatus();
        if (status == BatchStatus.COMPLETED) {
            System.out.println("Job completed successfully");
        } else {
            System.out.println("Job failed");
        }
    }

}
