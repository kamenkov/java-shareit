package ru.practicum.shareit.constraintvalidators;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.constraints.StartBeforeEndDateValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<StartBeforeEndDateValid, BookingDto> {

    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
        //ignored
    }

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}
