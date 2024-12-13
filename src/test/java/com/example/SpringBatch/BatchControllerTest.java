package com.example.SpringBatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.file.FlatFileParseException;

import com.example.SpringBatch.exceptioon.IncorrectCSVFileFormatException;

public class BatchControllerTest {
	
    @InjectMocks
    private BatchController batchController;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job job;

    @Mock
    private JobExecution jobExecution;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testBatchCompleted() throws Exception{
    	
    	when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
    	when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
    	
    	String response=batchController.startbatch();
    	
    	assertEquals("batch completed", response);
    	verify(jobLauncher, times(1)).run(any(Job.class), any(JobParameters.class));
    	
    	
    }
    @Test
    void testBatchFailedWithFlatFleParsexception() throws Exception {
    	when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);
    	when(jobExecution.getStatus()).thenReturn(BatchStatus.FAILED);
    	when(jobExecution.getAllFailureExceptions()).thenReturn(List.of(new FlatFileParseException("parsing error","test line")));
    	
    	
    	
    	assertThrows(IncorrectCSVFileFormatException.class, ()-> batchController.startbatch());
    	
    }


}
