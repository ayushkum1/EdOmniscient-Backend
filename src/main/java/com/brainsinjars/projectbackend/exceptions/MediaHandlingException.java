package com.brainsinjars.projectbackend.exceptions;

/**
 * @author Kartik Singhal
 * @since 07-03-21
 */

public class MediaHandlingException extends RuntimeException {
	private static final long serialVersionUID = -4905376868773920947L;

	public MediaHandlingException(String message) {
		super(message);
	}

}
