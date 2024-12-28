package com.example.SpringBatch;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import com.example.SpringBatch.config.KafkaConsumerConfig;

public class KafkaConsumerConfigTest {
	
	
	@Test
	public void consumerFactoryTest() {
		
		KafkaConsumerConfig kafkaConsumerConfig=Mockito.spy(new KafkaConsumerConfig("localhost:9092"));
		ConsumerFactory<String ,String > consumerFactory=kafkaConsumerConfig.consumerFactory();
		
		assertNotNull(consumerFactory,"Consumer factory should not null");
	}
	
	@Test
	public void concurrentKafkaListenerContainerFactory() {
		
		KafkaConsumerConfig kafkaConsumerConfig=new KafkaConsumerConfig("localhost:9092");
		
		ConcurrentKafkaListenerContainerFactory<String,String> concurrentKafkaListenerContainerFactory = kafkaConsumerConfig.concurrentKafkaListenerContainerFactory();
		
		assertNotNull(concurrentKafkaListenerContainerFactory,"concurrentKafkaListenerContainerFactory should not nul");
		
	}
	
}
