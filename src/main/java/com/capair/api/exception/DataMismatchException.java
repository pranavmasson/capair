package com.capair.api.exception;

public class DataMismatchException extends RuntimeException {
    
    public DataMismatchException() {
		super();
	}
	
	public DataMismatchException(String message) {
		super(message);
	}
}
