package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.handler.exception.NotFoundException.notFoundException;

@Validated
@Service
public class BookingService {

    private static final Sort.TypedSort<Booking> bookingSort = Sort.sort(Booking.class);
    private static final Sort END_DATE_DESC_SORT = bookingSort.by(Booking::getEndDate).descending();

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
            throw new IllegalArgumentException("Start date after end date");
        }
        AppUser booker = userService.getById(bookerId);
        booking.setBooker(booker);
        Item item = itemService.getItem(bookingDto.getItemId());
        if (Boolean.FALSE.equals(item.isAvailable())) {
            throw new IllegalArgumentException("Item unavailable");
        }
        if (item.getOwner().equals(booker)) {
            throw new NotFoundException("Booker cannot book his own item {0}", item.getId());
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
            throw new NotFoundException("not allowed", ownerId);
        }
        if (booking.getStatus().equals(BookingState.APPROVED)) {
            throw new IllegalArgumentException("Already approved");
        }
        booking.setStatus(Boolean.TRUE.equals(isApproved) ? BookingState.APPROVED : BookingState.REJECTED);
        bookingRepository.save(booking);
        return bookingMapper.bookingMapToDto(booking);
    }

    public BookingDto findById(Long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(notFoundException(ItemService.ITEM_NOT_FOUND_MESSAGE, bookingId));
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Not allowed", userId);
        }
        return bookingMapper.bookingMapToDto(booking);
    }

    public List<BookingDto> findAll(long userId, String stateString, @PositiveOrZero int from, @Positive int size) {
        AppUser user = userService.getById(userId);
        Pageable pageable = PageRequest.of(from / size, size, END_DATE_DESC_SORT);
        try {
            RequestState state = RequestState.valueOf(stateString);
            switch (state) {
                case PAST:
                    return bookingRepository.findBookingsByBookerAndEndDateIsBefore(user,
                                    LocalDateTime.now(),
                                    pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                case FUTURE:
                    return bookingRepository.findBookingsByBookerAndStartDateIsAfter(user,
                                    LocalDateTime.now(),
                                    pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                case CURRENT:
                    return bookingRepository.findBookingsByBookerAndStartDateIsBeforeAndEndDateIsAfter(user,
                                    LocalDateTime.now(),
                                    LocalDateTime.now(),
                                    pageable
                            )
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                case WAITING:
                    return bookingRepository.findBookingsByBookerAndStatus(user,
                                    BookingState.WAITING,
                                    pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                case REJECTED:
                    return bookingRepository.findBookingsByBookerAndStatus(user,
                                    BookingState.REJECTED,
                                    pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                default:
                    return bookingRepository.findBookingsByBooker(user, pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + stateString);
        }
    }

    public List<BookingDto> findAllForOwner(long userId, String stateString, @PositiveOrZero int from, @Positive int size) {
        AppUser user = userService.getById(userId);
        Pageable pageable = PageRequest.of(from / size, size, END_DATE_DESC_SORT);
        try {
            RequestState state = RequestState.valueOf(stateString);
            switch (state) {
                case PAST:
                    return bookingRepository.findBookingsByItem_OwnerAndEndDateIsBefore(user,
                                    LocalDateTime.now(),
                                    pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                case FUTURE:
                    return bookingRepository.findBookingsByItem_OwnerAndStartDateIsAfter(user,
                                    LocalDateTime.now(),
                                    pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                case CURRENT:
                    return bookingRepository.findBookingsByItem_OwnerAndStartDateIsBeforeAndEndDateIsAfter(user,
                                    LocalDateTime.now(),
                                    LocalDateTime.now(),
                                    pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                case WAITING:
                    return bookingRepository.findBookingsByItem_OwnerAndStatus(user,
                                    BookingState.WAITING,
                                    pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                case REJECTED:
                    return bookingRepository.findBookingsByItem_OwnerAndStatus(user,
                                    BookingState.REJECTED,
                                    pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
                default:
                    return bookingRepository.findBookingsByItem_Owner(user, pageable)
                            .map(bookingMapper::bookingMapToDto)
                            .getContent();
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + stateString);
        }
    }

    public boolean isUsersBookedItem(AppUser user, Item item) {
        return bookingRepository.countBookingsByBookerAndItemAndStatusAndEndDateIsBefore(
                user, item, BookingState.APPROVED, LocalDateTime.now()) > 0;
    }

    @Transactional(readOnly = true)
    public BookingForItemDto getLastBooking(Long id) {
        final Booking lastBooking = bookingRepository.findFirstByItem_IdAndEndDateIsBefore(id, LocalDateTime.now());
        return bookingMapper.bookingMapToForItemDto(lastBooking);
    }

    @Transactional(readOnly = true)
    public BookingForItemDto getNextBooking(Long id) {
        final Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartDateIsAfter(id, LocalDateTime.now());
        return bookingMapper.bookingMapToForItemDto(nextBooking);
    }
}
