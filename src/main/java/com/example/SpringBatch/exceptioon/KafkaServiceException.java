package com.example.SpringBatch.exceptioon;



public class KafkaServiceException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KafkaServiceException(String message) {
		
		super(message);
		
	}

}
