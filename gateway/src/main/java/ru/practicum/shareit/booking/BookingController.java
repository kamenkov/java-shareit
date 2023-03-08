package ru.practicum.shareit.booking;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id, @RequestHeader(X_LATER_USER_ID) long userId) {
        return bookingClient.findById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(X_LATER_USER_ID) long userId,
                                          @RequestParam(name = "state", defaultValue = "ALL") String state,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                          @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        return bookingClient.findAll(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllForOwner(@RequestHeader(X_LATER_USER_ID) long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        return bookingClient.findAllForOwner(userId, state, from, size);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@Valid @RequestBody BookingDto bookingDto,
                                         @RequestHeader(X_LATER_USER_ID) long bookerId) {
        return bookingClient.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable Long bookingId,
                                          @RequestParam(name = "approved") Boolean isApproved,
                                          @RequestHeader(X_LATER_USER_ID) long ownerId) {
        return bookingClient.approve(bookingId, isApproved, ownerId);
    }

}
