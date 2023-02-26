package ru.practicum.shareit.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ErrorHandlerTest {

    @Autowired
    ErrorHandler errorHandler;

    @Test
    void handleConstraintViolationException() {
        ConstraintViolationException constraintViolationException = new ConstraintViolationException(Set.of());
        ValidationErrorResponse validationErrorResponse =
                errorHandler.handleConstraintViolationException(constraintViolationException);
        assertEquals(0, validationErrorResponse.getViolations().size());

        Violation violation = new Violation("test", "test");
        assertNotNull(violation.getFieldName());
        assertNotNull(violation.getMessage());
    }
}