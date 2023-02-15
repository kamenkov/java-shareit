package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@DirtiesContext(classMode = AFTER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BookingServiceTest {

    @MockBean
    BookingRepository mockBookingRepository;
    @MockBean
    UserService mockUserService;
    @MockBean
    ItemService mockItemService;
    @Autowired
    BookingMapper bookingMapper;
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    BookingService bookingService;

    AppUser user;
    Item item;
    Booking booking;

    @BeforeEach
    void beforeEach() {
        user = Utils.getUser(1L);
        item = Utils.getItem(1L, user);
        Mockito.when(mockUserService.getById(-1L)).thenThrow(NotFoundException.class);
        Mockito.when(mockUserService.getById(1L)).thenReturn(Utils.getUser(1L));
        Mockito.when(mockUserService.getById(2L)).thenReturn(Utils.getUser(2L));
        Mockito.when(mockItemService.getItem(Mockito.anyLong()))
                .thenReturn(Utils.getItem(1L, Utils.getUser(1L)));
        Mockito.when(mockBookingRepository.findById(-1L)).thenThrow(NotFoundException.class);
        booking = Utils.getBooking(
                1L,
                Utils.getItem(1L, Utils.getUser(1L)),
                Utils.getUser(2L)
        );
        Mockito.when(mockBookingRepository.findById(1L)).thenReturn(
                Optional.of(booking)
        );
        Mockito.when(mockBookingRepository.findBookingsByBooker(Mockito.any(AppUser.class), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Mockito.when(mockBookingRepository.findBookingsByItem_Owner(Mockito.any(AppUser.class), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Mockito.when(mockBookingRepository.save(booking)).thenReturn(booking);
    }

    @Test
    void create() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setAvailable(true);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItem(itemDto);
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.MAX);
        bookingDto.setEnd(LocalDateTime.MAX);
        Booking booking = bookingMapper.dtoMapToBooking(bookingDto);
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.create(bookingDto, 1L));
        bookingService.create(bookingDto, 2L);
        Mockito.verify(mockBookingRepository, Mockito.times(1)).save(booking);
        Mockito.verifyNoMoreInteractions(mockBookingRepository);
    }

    @Test
    void approve() {
        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.approve(-1L, true, 1L));
        bookingService.approve(1L, true, 1L);
        Mockito.verify(mockBookingRepository, Mockito.times(2)).findById(Mockito.anyLong());
        Mockito.verify(mockBookingRepository, Mockito.times(1)).save(booking);
        Mockito.verifyNoMoreInteractions(mockBookingRepository);
    }

    @Test
    void findById() {
        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.findById(-1L, 1L));
        bookingService.findById(1L, 1L);
        Mockito.verify(mockBookingRepository, times(2)).findById(anyLong());

    }

    @Test
    void findAll() {
        assertThrows(NotFoundException.class, () -> bookingService.findAll(-1L, "ALL", 1, 3));
        bookingService.findAll(1L, "ALL", 1, 3);
        verify(mockUserService, times(2)).getById(anyLong());
        verify(mockBookingRepository, times(1)).findBookingsByBooker(eq(user), any());
    }

    @Test
    void findAllForOwner() {
        assertThrows(NotFoundException.class, () -> bookingService.findAllForOwner(-1L, "ALL", 1, 3));
        bookingService.findAllForOwner(1L, "ALL", 1, 3);
        verify(mockUserService, times(2)).getById(anyLong());
        verify(mockBookingRepository, times(1)).findBookingsByItem_Owner(eq(user), any());
    }

    @Test
    void isUsersBookedItem() {
        bookingService.isUsersBookedItem(user, item);
        verify(mockBookingRepository, times(1))
                .countBookingsByBookerAndItemAndStatusAndEndDateIsBefore(eq(user), eq(item), any(), any());
    }

    @Test
    void getLastBooking() {
        bookingService.getLastBooking(1L);
        verify(mockBookingRepository, times(1))
                .findFirstByItem_IdAndEndDateIsBefore(eq(1L), any());
    }

    @Test
    void getNextBooking() {
        bookingService.getNextBooking(1L);
        verify(mockBookingRepository, times(1))
                .findFirstByItem_IdAndStartDateIsAfter(eq(1L), any());
    }
}