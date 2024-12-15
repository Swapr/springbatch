package com.example.SpringBatch.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;

@Service
public class KafkaProducerService {
	
	public static final Logger logger= (Logger) LoggerFactory.getLogger(KafkaProducerService.class);
	
	
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void sendMessage(String topic,String message) {
		logger.info("message sent to kafka topic: "+topic+", and message is : "+message);
		
		kafkaTemplate.send(topic, message);
	
		
		
	}

}
