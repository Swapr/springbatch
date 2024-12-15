package com.example.SpringBatch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Steplistener implements StepExecutionListener {
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		
			System.out.println("job is waiting");
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			System.out.println("job is runnig ");
		
		
		return StepExecutionListener.super.afterStep(stepExecution);
	}

}
