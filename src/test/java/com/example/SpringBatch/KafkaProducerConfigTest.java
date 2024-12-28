package com.example.SpringBatch;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.example.SpringBatch.config.KafkaProducerConfig;

public class KafkaProducerConfigTest {
	
	
	@Test
	public void ProducerFactoryTest() {
		
		KafkaProducerConfig kafkaProducerConfig=new KafkaProducerConfig("localhost:9092");
		ProducerFactory<String, String> producerFactory = kafkaProducerConfig.producerFactory();
		
		assertNotNull(producerFactory,"kafkaproducr shuld not null");

	}
	
	@Test
	public void kafkaTemplateTest() {
		
		KafkaProducerConfig kafkaProducerConfig=new KafkaProducerConfig("localhost:9092");
		
		KafkaTemplate<String,String> kafkaTemplate = kafkaProducerConfig.kafkaTemplate();
		
		assertNotNull(kafkaTemplate,"kafkaTemplate shuld not be null");
		
	}
	
	

}


