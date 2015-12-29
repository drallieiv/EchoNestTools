package com.herazade.echonest.tools.storage;

import java.io.IOException;

/**
 * Custom Exception for Storage Errors
 * 
 * @author drallieiv
 *
 */
@SuppressWarnings("serial")
public class StorageException extends Exception {

	// Error Context
	private String context;

	// Error Code
	private String code;

	public StorageException(StorageError error) {
		super(error.getMsg());
		this.code = error.getCode();
		this.context = error.getContext();
	}
	
	public StorageException(StorageError error, Throwable cause) {
		super(error.getMsg(), cause);
		this.code = error.getCode();
		this.context = error.getContext();
	}

	public StorageException(String message) {
		super(message);
	}

	public StorageException(Throwable cause) {
		super(cause);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}



}
