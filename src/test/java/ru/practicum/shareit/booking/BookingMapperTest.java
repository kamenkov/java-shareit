package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.AppUser;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    @Test
    void bookingMapToForItemDto() {

        BookingForItemDto expectedDto = new BookingForItemDto();
        expectedDto.setId(1L);
        expectedDto.setBookerId(1L);

        Booking booking = new Booking();
        AppUser booker = new AppUser();
        booker.setId(1L);
        booking.setBooker(booker);
        booking.setId(1L);

        BookingMapper bookingMapper = new BookingMapperImpl();
        BookingForItemDto actualDto = bookingMapper.bookingMapToForItemDto(booking);

        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getBookerId(), actualDto.getBookerId());
    }
}