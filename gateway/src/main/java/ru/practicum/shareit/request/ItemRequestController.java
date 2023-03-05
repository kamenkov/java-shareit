package ru.practicum.shareit.request;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<Object> findOwnRequests(@RequestHeader(X_LATER_USER_ID) long userId) {
        return itemRequestClient.findOwnRequests(userId);
    }

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public ResponseEntity<Object> findOtherRequests(@RequestHeader(X_LATER_USER_ID) long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemRequestClient.findOtherRequests(userId, from, size);
    }


    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@RequestHeader(X_LATER_USER_ID) long userId,
                                   @PathVariable Long id) {
        return itemRequestClient.findById(id, userId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@NotNull @RequestBody ItemRequestDto itemRequestDto,
                                 @NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemRequestClient.create(itemRequestDto, userId);
    }
}
