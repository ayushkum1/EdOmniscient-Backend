package com.brainsinjars.projectbackend.exceptions;

/**
 * @author Kanchan Harjani
 * @since 08-03-2021
 */
public class UserNotFoundException extends Exception {
	public UserNotFoundException(String msg) {
		super(msg);
	}
}
