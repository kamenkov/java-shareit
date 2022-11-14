package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper
public interface BookingMapper {

    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    BookingDto bookingMapToDto(Booking booking);

    @Mapping(source = "start", target = "startDate")
    @Mapping(source = "end", target = "endDate")
    Booking dtoMapToBooking(BookingDto bookingDto);
}
