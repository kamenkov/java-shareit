package ru.practicum.shareit.item;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.handler.exception.ForbiddenException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.handler.exception.NotFoundException.notFoundException;

@Validated
@Service
public class ItemService {

    public static final String ITEM_NOT_FOUND_MESSAGE = "Item {0} not found";
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final BookingService bookingService;
    private final CommentService commentService;

    private final ItemRequestService itemRequestService;
    private final ItemRepository itemRepository;

    public ItemService(ItemMapper itemMapper,
                       UserService userService,
                       @Lazy BookingService bookingService,
                       @Lazy CommentService commentService,
                       @Lazy ItemRequestService itemRequestService,
                       ItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.userService = userService;
        this.bookingService = bookingService;
        this.commentService = commentService;
        this.itemRequestService = itemRequestService;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> findAll(Long searcherId, @PositiveOrZero int from, @Positive int size) {
        final AppUser searcher = userService.getById(searcherId);
        final Pageable pageable = PageRequest.of(from / size, size);
        List<ItemDto> itemDtos = itemRepository.findAllByOwnerIdOrderByIdAsc(searcher.getId(), pageable).stream()
                .map(itemMapper::itemMapToDto)
                .collect(Collectors.toList());
        for (ItemDto itemDto : itemDtos) {
            itemDto.setLastBooking(bookingService.getLastBooking(itemDto.getId()));
            itemDto.setNextBooking(bookingService.getNextBooking(itemDto.getId()));
            itemDto.setComments(commentService.getCommentsForItem(itemDto.getId()));
        }
        return itemDtos;
    }

    public ItemDto findById(Long searcherId, Long id) {
        final Item item = getItem(id);
        ItemDto itemDto = itemMapper.itemMapToDto(item);
        if (item.getOwner().getId().equals(searcherId)) {
            itemDto.setLastBooking(bookingService.getLastBooking(itemDto.getId()));
            itemDto.setNextBooking(bookingService.getNextBooking(itemDto.getId()));
        }
        itemDto.setComments(commentService.getCommentsForItem(itemDto.getId()));
        return itemDto;
    }


    public ItemDto create(@Valid ItemDto itemDto, Long userId) {
        Item item = itemMapper.dtoMapToItem(itemDto);
        AppUser owner = userService.getById(userId);
        item.setOwner(owner);
        final Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            ItemRequest itemRequest = itemRequestService.getById(requestId);
            item.setItemRequest(itemRequest);
        }
        item = itemRepository.save(item);
        return itemMapper.itemMapToDto(item);
    }

    public ItemDto update(Long id, ItemDto itemDto, Long userId) {
        final Item item = getItem(id);
        validateOwner(id, userId, item);
        final String dtoDescription = itemDto.getDescription();
        if (dtoDescription != null) {
            item.setDescription(dtoDescription);
        }
        final String dtoName = itemDto.getName();
        if (dtoName != null) {
            item.setName(dtoName);
        }
        final Boolean dtoIsAvailable = itemDto.isAvailable();
        if (dtoIsAvailable != null) {
            item.setAvailable(dtoIsAvailable);
        }
        itemRepository.save(item);
        return itemMapper.itemMapToDto(item);
    }

    public Item getItem(Long id) {
        return itemRepository
                .findById(id)
                .orElseThrow(notFoundException(ITEM_NOT_FOUND_MESSAGE, id));
    }

    public void removeItem(Long id, Long userId) {
        final Item item = getItem(id);
        validateOwner(id, userId, item);
        itemRepository.deleteById(id);
    }

    public List<ItemDto> search(String query, @PositiveOrZero int from, @Positive int size) {
        if (query.isBlank()) {
            return Collections.emptyList();
        }
        final Pageable pageable = PageRequest.of(from / size, size);
        return itemRepository.findAll(pageable).stream()
                .filter(i -> i.getDescription().toLowerCase().contains(query.toLowerCase())
                        || i.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(Item::isAvailable)
                .map(itemMapper::itemMapToDto)
                .collect(Collectors.toList());
    }

    public List<ItemForItemRequestDto> findByRequest(Long itemRequestId) {
        return itemRepository.findAllByItemRequest_Id(itemRequestId).stream()
                .map(itemMapper::itemMapToForItemRequestDto)
                .collect(Collectors.toList());
    }

    private void validateOwner(Long id, Long userId, Item item) {
        AppUser requester = userService.getById(userId);
        final AppUser owner = item.getOwner();
        if (owner != null && !owner.equals(requester)) {
            throw new ForbiddenException("User {0} is not owner of this item {1}", userId, id);
        }
    }
}
