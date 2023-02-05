package ru.practicum.shareit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.AppUserDto;
import ru.practicum.shareit.user.model.AppUser;

import java.time.LocalDateTime;

public class Utils {

    public static AppUser getUser(Long id) {
        AppUser user = new AppUser();
        user.setId(id);
        user.setName("Name" + id);
        user.setEmail("email" + id + "@mail.ru");
        return user;
    }

    public static AppUserDto getUserDto(Long id) {
        AppUserDto userDto = new AppUserDto();
        userDto.setId(id);
        userDto.setName("Name" + id);
        userDto.setEmail("email" + id + "@mail.ru");
        return userDto;
    }

    public static Item getItem(Long id, AppUser owner) {
        Item item = new Item();
        item.setId(id);
        item.setName("Item" + id);
        item.setDescription("Description" + id);
        item.setOwner(owner);
        item.setAvailable(true);
        return item;
    }

    public static ItemRequest getItemRequest(Long id, AppUser creator) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(LocalDateTime.MIN);
        itemRequest.setId(id);
        itemRequest.setDescription("Description" + id);
        itemRequest.setCreator(creator);
        return itemRequest;
    }

    public static Booking getBooking(Long id, Item item, AppUser booker) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setItem(item);
        booking.setStatus(BookingState.WAITING);
        booking.setBooker(booker);
        return booking;
    }

}