package com.herazade.echonest.tools.looper;

/**
 * Typed exception for scrapping
 * 
 * @author drallieiv
 *
 */
public class ScrapperException extends Exception {

	public ScrapperException() {
	}

	public ScrapperException(String message) {
		super(message);
	}

	public ScrapperException(Throwable cause) {
		super(cause);
	}

	public ScrapperException(String message, Throwable cause) {
		super(message, cause);

	}

	public ScrapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
