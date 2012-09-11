package org.beequeue.buzz;

import org.beequeue.util.BeeException;

public class BuzzException extends BeeException {
	private static final long serialVersionUID = 1L;
	public final int statusCode;

	public BuzzException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public BuzzException(int statusCode, Throwable cause) {
		super(cause);
		this.statusCode = statusCode;
	}
	
	public BuzzException(int statusCode, String message, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	
}
