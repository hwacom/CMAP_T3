package com.cmap.exception;

public class CommandExecuteException extends Exception {

	private static final long serialVersionUID = 3279651784042185727L;

	public CommandExecuteException() {
		super();
	}
	
	public CommandExecuteException(String message) {
        super(message);
    }
	
	public CommandExecuteException(Throwable cause) {
        super(cause);
    }
}
