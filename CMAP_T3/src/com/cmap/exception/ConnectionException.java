package com.cmap.exception;

public class ConnectionException extends Exception {

	private static final long serialVersionUID = -5157745539384280175L;

	public ConnectionException() {
		super();
	}
	
	public ConnectionException(String message) {
        super(message);
    }
	
	public ConnectionException(Throwable cause) {
        super(cause);
    }
}
