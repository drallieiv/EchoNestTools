package com.herazade.echonest.tools.core.remix.strategy;

/**
 * Custom Exception for Remix
 * 
 * @author drallieiv
 *
 */
@SuppressWarnings("serial")
public class RemixException extends RuntimeException {

	public RemixException() {
	}

	public RemixException(String message) {
		super(message);
	}

	public RemixException(Throwable cause) {
		super(cause);
	}

	public RemixException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemixException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
