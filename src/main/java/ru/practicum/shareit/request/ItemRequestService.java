package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.handler.exception.NotFoundException.notFoundException;

@Validated
@Service
public class ItemRequestService {

    private static final Sort.TypedSort<ItemRequest> itemRequestSort = Sort.sort(ItemRequest.class);
    private static final Sort CREATED_DESC_SORT = itemRequestSort.by(ItemRequest::getCreated).descending();
    public static final String REQUEST_NOT_FOUND_MESSAGE = "Request {0} not found";

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    private final ItemService itemService;
    private final ItemRequestMapper itemRequestMapper;

    public ItemRequestService(ItemRequestRepository itemRequestRepository,
                              UserService userService,
                              ItemService itemService,
                              ItemRequestMapper itemRequestMapper) {
        this.itemRequestRepository = itemRequestRepository;
        this.userService = userService;
        this.itemService = itemService;
        this.itemRequestMapper = itemRequestMapper;
    }

    public List<ItemRequestDto> findOwnRequests(long userId) {
        AppUser creator = userService.getById(userId);
        final List<ItemRequestDto> itemRequestDtos = itemRequestRepository.findAllByCreator(creator, CREATED_DESC_SORT)
                .stream()
                .map(itemRequestMapper::itemRequestMapToDto)
                .collect(Collectors.toList());
        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            itemRequestDto.setItems(itemService.findByRequest(itemRequestDto.getId()));
        }
        return itemRequestDtos;
    }

    public List<ItemRequestDto> findOtherRequests(long userId, String fromString, String sizeString) {
        try {
            int from = Integer.parseInt(fromString);
            int size = Integer.parseInt(sizeString);
            Pageable pageable = PageRequest.of(from, size).withSort(CREATED_DESC_SORT);
            AppUser user = userService.getById(userId);
            final List<ItemRequestDto> itemRequestDtos = itemRequestRepository.findAllByCreatorNot(user, pageable)
                    .stream()
                    .map(itemRequestMapper::itemRequestMapToDto)
                    .collect(Collectors.toList());
            for (ItemRequestDto itemRequestDto : itemRequestDtos) {
                itemRequestDto.setItems(itemService.findByRequest(itemRequestDto.getId()));
            }
            return itemRequestDtos;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("From and Size must be a numbers.");
        }
    }

    public ItemRequestDto findById(long userId, Long id) {
        AppUser creator = userService.getById(userId);
        ItemRequest itemRequest = getById(id);
        ItemRequestDto itemRequestDto = itemRequestMapper.itemRequestMapToDto(itemRequest);
        itemRequestDto.setItems(itemService.findByRequest(itemRequestDto.getId()));
        return itemRequestDto;
    }

    public ItemRequest getById(Long id) {
        return itemRequestRepository.findById(id)
                .orElseThrow(notFoundException(REQUEST_NOT_FOUND_MESSAGE, id));
    }

    public ItemRequestDto create(@Valid ItemRequestDto itemRequestDto, long userId) {
        AppUser creator = userService.getById(userId);
        ItemRequest itemRequest = itemRequestMapper.dtoMapToItemRequest(itemRequestDto);
        itemRequest.setCreator(creator);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        return itemRequestMapper.itemRequestMapToDto(itemRequest);
    }
}
