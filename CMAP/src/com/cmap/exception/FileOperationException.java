package com.cmap.exception;

public class FileOperationException extends Exception {

	private static final long serialVersionUID = 3279651784042185727L;

	public FileOperationException() {
		super();
	}
	
	public FileOperationException(String message) {
        super(message);
    }
	
	public FileOperationException(Throwable cause) {
        super(cause);
    }
}
