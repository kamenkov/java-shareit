package ru.practicum.shareit.handler;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {}
