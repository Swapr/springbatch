package com.example.SpringBatch.exceptioon;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler (IncorrectCSVFileFormatException.class)
	public ResponseEntity<String> IncorrectCSVFileFormat(IncorrectCSVFileFormatException ex){
		
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler (KafkaServiceException.class)
	public void kafkaServiceException() {
		
		System.out.println("kafka server has issue please check");
	}
}
