package ru.practicum.shareit.booking;


import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.handler.exception.ForbiddenException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import javax.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.handler.exception.NotFoundException.notFoundException;

@Validated
@Service
public class BookingService {

    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingRepository bookingRepository;

    public BookingService(BookingMapper bookingMapper,
                          UserService userService,
                          ItemService itemService, BookingRepository bookingRepository) {
        this.bookingMapper = bookingMapper;
        this.userService = userService;
        this.itemService = itemService;
        this.bookingRepository = bookingRepository;
    }

    public BookingDto create(@Valid BookingDto bookingDto, Long bookerId) {
        Booking booking = bookingMapper.dtoMapToBooking(bookingDto);
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new IllegalArgumentException("not allowed");
        }
        AppUser booker = userService.getById(bookerId);
        booking.setBooker(booker);
        Item item = itemService.getItem(bookingDto.getItemId());
        if (Boolean.FALSE.equals(item.isAvailable())) {
            throw new IllegalArgumentException("not allowed");
        }
        booking.setItem(item);
        booking.setStatus(BookingState.WAITING);
        booking = bookingRepository.save(booking);
        return bookingMapper.bookingMapToDto(booking);
    }

    public BookingDto approve(Long bookingId, Boolean isApproved, Long ownerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(notFoundException(ItemService.ITEM_NOT_FOUND_MESSAGE, bookingId));
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("not allowed", ownerId);
        }
        booking.setStatus(Boolean.TRUE.equals(isApproved) ? BookingState.APPROVED : BookingState.REJECTED);
        bookingRepository.save(booking);
        return bookingMapper.bookingMapToDto(booking);
    }

    public BookingDto findById(Long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(notFoundException(ItemService.ITEM_NOT_FOUND_MESSAGE, bookingId));
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new ForbiddenException("not allowed", userId);
        }
        return bookingMapper.bookingMapToDto(booking);
    }

    public List<BookingDto> findAll(long userId, RequestState state) {
        AppUser user = userService.getById(userId);
        switch (state) {
            case PAST:
                return bookingRepository.findBookingsByBookerAndEndDateIsBeforeOrderByEndDate(user, LocalDateTime.now())
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByBookerAndStartDateIsAfterOrderByEndDate(user, LocalDateTime.now())
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByBookerAndStartDateIsBeforeAndEndDateIsAfterOrderByEndDate(
                        user, LocalDateTime.now(), LocalDateTime.now())
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByBookerAndStatusOrderByEndDate(user, BookingState.WAITING)
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByBookerAndStatusOrderByEndDate(user, BookingState.REJECTED)
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            default:
                return bookingRepository.findBookingsByBookerOrderByEndDate(user).stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
        }
    }

    public List<BookingDto> findAllForOwner(long userId, RequestState state) {
        AppUser user = userService.getById(userId);
        switch (state) {
            case PAST:
                return bookingRepository.findBookingsByItem_OwnerAndEndDateIsBeforeOrderByEndDate(user, LocalDateTime.now())
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingsByItem_OwnerAndStartDateIsAfterOrderByEndDate(user, LocalDateTime.now())
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingsByItem_OwnerAndStartDateIsBeforeAndEndDateIsAfterOrderByEndDate(
                                user, LocalDateTime.now(), LocalDateTime.now())
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingsByItem_OwnerAndStatusOrderByEndDate(user, BookingState.WAITING)
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingsByItem_OwnerAndStatusOrderByEndDate(user, BookingState.REJECTED)
                        .stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
            default:
                return bookingRepository.findBookingsByItem_OwnerOrderByEndDate(user).stream()
                        .map(bookingMapper::bookingMapToDto)
                        .collect(Collectors.toList());
        }
    }
}
