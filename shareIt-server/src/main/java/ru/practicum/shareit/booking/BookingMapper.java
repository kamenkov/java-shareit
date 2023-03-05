package ru.practicum.shareit.booking;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper
public interface BookingMapper {

    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    BookingDto bookingMapToDto(Booking booking);

    @InheritInverseConfiguration
    Booking dtoMapToBooking(BookingDto bookingDto);

    @Mapping(source = "booking.booker.id", target = "bookerId")
    BookingForItemDto bookingMapToForItemDto(Booking booking);
}
