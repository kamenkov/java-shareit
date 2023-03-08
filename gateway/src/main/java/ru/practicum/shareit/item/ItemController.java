package ru.practicum.shareit.item;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping("/items")
public class ItemController {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(X_LATER_USER_ID) long userId,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                          @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemClient.findAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@RequestHeader(X_LATER_USER_ID) long userId,
                                           @PathVariable Long id) {
        return itemClient.findById(id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "text") String query,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "20") int size,
                                         @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemClient.search(query, userId, from, size);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto itemDto,
                                         @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemClient.create(itemDto, userId);
    }

    @PostMapping(path = "/{id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addComment(@PathVariable Long id,
                                             @Valid @RequestBody CommentRequestDto commentDto,
                                             @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemClient.addComment(id, commentDto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id,
                                         @RequestBody ItemDto itemDto,
                                         @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemClient.update(id, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id,
                           @RequestHeader(X_LATER_USER_ID) long userId) {
        itemClient.removeItem(id, userId);
    }

}
