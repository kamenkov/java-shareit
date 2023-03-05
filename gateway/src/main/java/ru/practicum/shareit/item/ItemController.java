package ru.practicum.shareit.item;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/items")
public class ItemController {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<Object> findAll(@NotNull @RequestHeader(X_LATER_USER_ID) long userId,
                                          @RequestParam(name = "from", defaultValue = "0") int from,
                                          @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemClient.findAll(userId, from, size);
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@NotNull @RequestHeader(X_LATER_USER_ID) long userId,
                                           @PathVariable Long id) {
        return itemClient.findById(id, userId);
    }

    @Transactional(readOnly = true)
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "text") String query,
                                         @RequestParam(name = "from", defaultValue = "0") int from,
                                         @RequestParam(name = "size", defaultValue = "20") int size,
                                         @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemClient.search(query, userId, from, size);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@NotNull @RequestBody ItemDto itemDto,
                                         @NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemClient.create(itemDto, userId);
    }

    @PostMapping(path = "/{id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addComment(@PathVariable Long id,
                                         @NotNull @RequestBody CommentRequestDto commentDto,
                                         @NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemClient.addComment(id, commentDto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id,
                                         @NotNull @RequestBody ItemDto itemDto,
                                         @NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemClient.update(id, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id,
                           @NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        itemClient.removeItem(id, userId);
    }

}
