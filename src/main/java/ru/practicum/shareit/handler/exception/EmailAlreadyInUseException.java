package ru.practicum.shareit.handler.exception;

public class EmailAlreadyInUseException extends IllegalArgumentException {

    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
