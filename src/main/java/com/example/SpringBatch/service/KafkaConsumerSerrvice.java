package com.example.SpringBatch.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerSerrvice {
	
	
	@KafkaListener(topics = "topic1",groupId = "group_id_1")
	public void consumeTopicJobStatus(String message) {
		
		System.out.println("message consumed : "+message);
		
	}

}
