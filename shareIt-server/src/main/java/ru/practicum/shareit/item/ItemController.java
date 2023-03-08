package ru.practicum.shareit.item;

import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    private final CommentService commentService;

    public ItemController(ItemService itemService, CommentService commentService) {
        this.itemService = itemService;
        this.commentService = commentService;
    }

    @Transactional(readOnly = true)
    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(X_LATER_USER_ID) long userId,
                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                 @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemService.findAll(userId, from, size);
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ItemDto findById(@RequestHeader(X_LATER_USER_ID) long userId,
                            @PathVariable Long id) {
        return itemService.findById(userId, id);
    }

    @Transactional(readOnly = true)
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text") String query,
                                @RequestParam(name = "from", defaultValue = "0") int from,
                                @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemService.search(query, from, size);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemDto create(@RequestBody ItemDto itemDto,
                          @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemService.create(itemDto, userId);
    }

    @PostMapping(path = "/{id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentResponseDto addComment(@PathVariable Long id,
                                         @RequestBody CommentRequestDto commentDto,
                                         @RequestHeader(X_LATER_USER_ID) long userId) {
        return commentService.addComment(id, commentDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable Long id,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemService.update(id, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id,
                           @RequestHeader(X_LATER_USER_ID) long userId) {
        itemService.removeItem(id, userId);
    }

}
