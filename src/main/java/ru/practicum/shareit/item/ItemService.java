package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.handler.exception.ForbiddenException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.handler.exception.NotFoundException.notFoundException;

@Validated
@Service
public class ItemService {

    public static final String ITEM_NOT_FOUND_MESSAGE = "Item {0} not found";
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public ItemService(ItemMapper itemMapper, UserService userService, ItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    public List<ItemDto> findAll(Long searcherId) {
        final User searcher = userService.getById(searcherId);
        return itemRepository.findAll().stream()
                .filter(i -> i.getOwner().equals(searcher))
                .map(itemMapper::itemMapToDto)
                .collect(Collectors.toList());
    }

    public ItemDto findById(Long id) {
        final Item item = getItem(id);
        return itemMapper.itemMapToDto(item);
    }


    public ItemDto create(@Valid ItemDto itemDto, Long userId) {
        Item item = itemMapper.dtoMapToItem(itemDto);
        User owner = userService.getById(userId);
        item.setOwner(owner);
        item = itemRepository.createItem(item);
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
        itemRepository.updateItem(id, item);
        return itemMapper.itemMapToDto(item);
    }

    private Item getItem(Long id) {
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
        User requester = userService.getById(userId);
        final User owner = item.getOwner();
        if (owner != null && !owner.equals(requester)) {
            throw new ForbiddenException("User {0} is not owner of this item {1}", userId, id);
        }
    }

    public List<ItemDto> search(String query) {
        return itemRepository.findAll().stream()
                .filter(i -> i.getDescription().toLowerCase().contains(query.toLowerCase())
                        || i.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(Item::isAvailable)
                .map(itemMapper::itemMapToDto)
                .collect(Collectors.toList());
    }
}
