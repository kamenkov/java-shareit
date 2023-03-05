package ru.practicum.shareit.handler.exception;

import java.text.MessageFormat;

public abstract class ShareItException extends RuntimeException {

    protected ShareItException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }
}
