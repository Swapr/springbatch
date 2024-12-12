package com.example.SpringBatch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Steplistener implements StepExecutionListener {
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		
		return StepExecutionListener.super.afterStep(stepExecution);
	}

}
