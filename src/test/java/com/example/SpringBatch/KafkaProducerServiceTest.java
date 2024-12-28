package com.example.SpringBatch;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.SpringBatch.service.KafkaProducerService;


public class KafkaProducerServiceTest {
	
	
	@Mock
	KafkaTemplate<String, String> kafkaTemplate;

	
	   @BeforeEach
	    void setUp() {
	        // Initialize mocks
	        MockitoAnnotations.openMocks(this);
	    }
	
	@Test
	public void  sendMessageTest() {
		
		 KafkaProducerService kafkaProducerService = new KafkaProducerService(kafkaTemplate);
		
		kafkaProducerService.sendMessage("topic1", "test meassage for kafkaProducerService");
		
		 verify(kafkaTemplate, times(1)).send("topic1","test meassage for kafkaProducerService");
		
	}

}
