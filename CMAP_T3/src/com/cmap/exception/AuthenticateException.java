package com.cmap.exception;

public class AuthenticateException extends Exception {
	private static final long serialVersionUID = -6116944015785950503L;

	public AuthenticateException() {
		super();
	}
	
	public AuthenticateException(String message) {
        super(message);
    }
	
	public AuthenticateException(Throwable cause) {
        super(cause);
    }
}
