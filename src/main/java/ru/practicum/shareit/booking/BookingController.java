package ru.practicum.shareit.booking;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    public BookingDto findById(@PathVariable Long id, @RequestHeader(X_LATER_USER_ID) long userId) {
        return bookingService.findById(id, userId);
    }

    @GetMapping
    public List<BookingDto> findAll(@RequestHeader(X_LATER_USER_ID) long userId,
                                    @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAll(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllForOwner(@RequestHeader(X_LATER_USER_ID) long userId,
                                            @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllForOwner(userId, state);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookingDto create(@NotNull @RequestBody BookingDto bookingDto,
                             @NotNull @RequestHeader(X_LATER_USER_ID) long bookerId) {
        return bookingService.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable Long bookingId,
                              @RequestParam(name = "approved") Boolean isApproved,
                              @NotNull @RequestHeader(X_LATER_USER_ID) long ownerId) {
        return bookingService.approve(bookingId, isApproved, ownerId);
    }

}
