package com.brainsinjars.projectbackend.dto;

/**
 * @author Nilesh Pandit
 * @since 06-03-2021
 */

public class MessageDto {
    private String message;
    private MessageType messageType;

    public MessageDto(String message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
