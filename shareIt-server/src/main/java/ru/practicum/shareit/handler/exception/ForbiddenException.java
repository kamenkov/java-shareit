package ru.practicum.shareit.handler.exception;

public class ForbiddenException extends ShareItException {
    public ForbiddenException(String message, Object... args) {
        super(message, args);
    }
}
