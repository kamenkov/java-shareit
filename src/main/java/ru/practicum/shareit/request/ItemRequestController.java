package ru.practicum.shareit.request;

import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @Transactional(readOnly = true)
    @GetMapping
    public List<ItemRequestDto> findOwnRequests(@RequestHeader(X_LATER_USER_ID) long userId) {
        return itemRequestService.findOwnRequests(userId);
    }

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public List<ItemRequestDto> findOtherRequests(@RequestHeader(X_LATER_USER_ID) long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemRequestService.findOtherRequests(userId, from, size);
    }


    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ItemRequestDto findById(@RequestHeader(X_LATER_USER_ID) long userId,
                                   @PathVariable Long id) {
        return itemRequestService.findById(userId, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ItemRequestDto create(@NotNull @RequestBody ItemRequestDto itemRequestDto,
                                 @NotNull @RequestHeader(X_LATER_USER_ID) long userId) {
        return itemRequestService.create(itemRequestDto, userId);
    }
}
