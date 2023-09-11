package com.capair.api.exception;

public class ServerUnavailableException extends RuntimeException{
    
    public ServerUnavailableException() {
        super();
	}
	
	public ServerUnavailableException(String message) {
		super(message);
	}

}
