package ru.practicum.shareit.item;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    List<ItemDto> findAll(@NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto findById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text") String query) {
        return itemService.search(query);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ItemDto create(@NotNull @RequestBody ItemDto itemDto,
                          @NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable Long id,
                          @NotNull @RequestBody ItemDto itemDto,
                          @NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemService.update(id, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id,
                           @NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        itemService.removeItem(id, userId);
    }

}
