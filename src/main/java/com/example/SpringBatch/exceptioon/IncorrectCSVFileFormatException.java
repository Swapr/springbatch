package com.example.SpringBatch.exceptioon;


public class IncorrectCSVFileFormatException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IncorrectCSVFileFormatException(String meessage) {
		super(meessage);
		
	}

}
