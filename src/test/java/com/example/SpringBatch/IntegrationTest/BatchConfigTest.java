package com.example.SpringBatch.IntegrationTest;


import com.example.SpringBatch.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

class BatchConfigTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testBatchJobIntegration() throws Exception {
        // Set up job parameters
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("userFile", "classpath:customers-100.csv")
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        // Launch the job
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);

        // Assert the job status
        assertNotNull(jobExecution);
        assertEquals("COMPLETED", jobExecution.getStatus().toString());

        // Verify data is inserted into the database
        long count = userRepository.count(); // Ensure userRepository has access to the database
        assertTrue(count > 0, "Data should be saved to the database.");
    }
}
