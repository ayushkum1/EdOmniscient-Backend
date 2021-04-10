package com.brainsinjars.projectbackend.exceptions;

/**
 * @author Nilesh Pandit
 * @since 06-03-2021
 */

public class RecordNotFoundException extends Exception {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
