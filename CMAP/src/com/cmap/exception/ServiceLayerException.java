package com.cmap.exception;

public class ServiceLayerException extends Exception {
	private static final long serialVersionUID = -6116944015785950503L;

	public ServiceLayerException() {
		super();
	}
	
	public ServiceLayerException(String message) {
        super(message);
    }
	
	public ServiceLayerException(Throwable cause) {
        super(cause);
    }
}
