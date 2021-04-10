package com.brainsinjars.projectbackend.exceptions;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

public class RecordAlreadyExistsException extends Exception {
    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
