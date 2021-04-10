package com.brainsinjars.projectbackend.util;

import com.brainsinjars.projectbackend.dto.MessageDto;
import com.brainsinjars.projectbackend.dto.MessageType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

public class ResponseUtils {
    public static final String INVALID_ID_MESSAGE = "Invalid ID";
    public static final String FORBIDDEN_MESSAGE = "You do not have access to this resource";

    public static ResponseEntity<MessageDto> successResponse(String message) {
        return ResponseEntity.ok(new MessageDto(message, MessageType.SUCCESS));
    }

    public static ResponseEntity<MessageDto> warningResponse(String message) {
        return ResponseEntity.ok(new MessageDto(message, MessageType.WARNING));
    }

    public static ResponseEntity<MessageDto> infoResponse(String message) {
        return ResponseEntity.ok(new MessageDto(message, MessageType.INFO));
    }

    public static ResponseEntity<MessageDto> badRequestResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDto(message, MessageType.ERROR));
    }

    public static ResponseEntity<MessageDto> forbiddenResponse(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDto(message, MessageType.ERROR));
    }

    public static ResponseEntity<MessageDto> notFoundResponse(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(message, MessageType.ERROR));
    }

    public static ResponseEntity<MessageDto> internalServerErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageDto(message, MessageType.ERROR));
    }
    
}
