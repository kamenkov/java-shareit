package ru.practicum.shareit.item;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.handler.exception.ForbiddenException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import javax.validation.Valid;
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
    private final ItemRepository itemRepository;

    public ItemService(ItemMapper itemMapper,
                       UserService userService,
                       @Lazy BookingService bookingService,
                       @Lazy CommentService commentService,
                       ItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.userService = userService;
        this.bookingService = bookingService;
        this.commentService = commentService;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<ItemDto> findAll(Long searcherId) {
        final AppUser searcher = userService.getById(searcherId);
        List<ItemDto> itemDtos = itemRepository.findAll().stream()
                .filter(i -> i.getOwner().equals(searcher))
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

    private void validateOwner(Long id, Long userId, Item item) {
        AppUser requester = userService.getById(userId);
        final AppUser owner = item.getOwner();
        if (owner != null && !owner.equals(requester)) {
            throw new ForbiddenException("User {0} is not owner of this item {1}", userId, id);
        }
    }

    public List<ItemDto> search(String query) {
        if (query.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findAll().stream()
                .filter(i -> i.getDescription().toLowerCase().contains(query.toLowerCase())
                        || i.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(Item::isAvailable)
                .map(itemMapper::itemMapToDto)
                .collect(Collectors.toList());
    }
}
