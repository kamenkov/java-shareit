package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.handler.exception.ForbiddenException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@DirtiesContext(classMode = AFTER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ItemServiceTest {

    @MockBean
    UserService mockUserService;
    @MockBean
    ItemRequestService mockItemRequestService;
    @MockBean
    ItemRepository mockItemRepository;

    @MockBean
    BookingRepository mockBookingRepository;
    @Autowired
    ItemService itemService;

    @Autowired
    ItemMapper itemMapper;

    AppUser user;
    Item item;
    Booking booking;

    @BeforeEach
    void init() {
        user = Utils.getUser(1L);
        item = Utils.getItem(1L, user);
        Mockito.when(mockUserService.getById(1L)).thenReturn(user);
        Mockito.when(mockUserService.getById(2L)).thenReturn(Utils.getUser(2L));
        Mockito.when(mockItemRequestService.getById(Mockito.anyLong())).thenReturn(Utils.getItemRequest(1L, user));
        Mockito.when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));
        final List<Item> items = List.of(Utils.getItem(1L, user),
                Utils.getItem(2L, user),
                Utils.getItem(3L, user),
                Utils.getItem(4L, user));
        Mockito.when(mockItemRepository.findAllByOwnerIdOrderByIdAsc(eq(1L), Mockito.any(Pageable.class)))
                .thenReturn(items);
        Mockito.when(mockItemRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(items));
        Mockito.when(mockItemRepository.save(item)).thenReturn(item);
        booking = Utils.getBooking(
                1L,
                Utils.getItem(1L, Utils.getUser(1L)),
                Utils.getUser(2L)
        );
        Mockito.when(mockBookingRepository.findBookingsByBooker(Mockito.any(AppUser.class), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
    }

    @Test
    void findById() {
        itemService.findById(1L, 1L);
        Mockito.verify(mockItemRepository, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(mockItemRepository);
    }

    @Test
    void findAll() {
        itemService.findAll(1L, 0, 10);
        Mockito.verify(mockItemRepository, Mockito.times(1))
                .findAllByOwnerIdOrderByIdAsc(eq(1L), Mockito.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(mockItemRepository);
    }

    @Test
    void create() {
        ItemDto itemDto = itemMapper.itemMapToDto(item);
        itemDto.setRequestId(1L);
        ItemDto created = itemService.create(itemDto, user.getId());
        Assertions.assertEquals(itemDto.getId(), created.getId());
        Assertions.assertEquals(itemDto.getName(), created.getName());
        Assertions.assertEquals(itemDto.getDescription(), created.getDescription());
        Mockito.verify(mockItemRepository, Mockito.times(1)).save(item);
        Mockito.verifyNoMoreInteractions(mockItemRepository);
    }

    @Test
    void update() {
        ItemDto itemDto = itemMapper.itemMapToDto(item);
        ItemDto created = itemService.update(1L, itemDto, user.getId());
        Assertions.assertEquals(itemDto.getId(), created.getId());
        Assertions.assertEquals(itemDto.getName(), created.getName());
        Assertions.assertEquals(itemDto.getDescription(), created.getDescription());
        Mockito.verify(mockItemRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(mockItemRepository, Mockito.times(1)).save(item);
        Mockito.verifyNoMoreInteractions(mockItemRepository);
    }

    @Test
    void updateWrongOwner() {
        ItemDto itemDto = itemMapper.itemMapToDto(item);
        Assertions.assertThrows(ForbiddenException.class, () -> itemService.update(1L, itemDto, 2L));
    }

    @Test
    void search() {
        Assertions.assertEquals(Collections.emptyList(), itemService.search("", 0, 1));
        List<ItemDto> returnedItems = itemService.search("3", 0, 1);
        Assertions.assertEquals(1, returnedItems.size());
        Assertions.assertEquals(3L, returnedItems.get(0).getId());
        Mockito.verify(mockItemRepository, Mockito.times(1))
                .findAll(Mockito.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(mockItemRepository);
    }

    @Test
    void findByRequest() {
        itemService.findByRequest(1L);
        Mockito.verify(mockItemRepository, Mockito.times(1))
                .findAllByItemRequest_Id(1L);
        Mockito.verifyNoMoreInteractions(mockItemRepository);
    }

    @Test
    void removeItem() {
        itemService.removeItem(1L, 1L);
        Mockito.verify(mockItemRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(mockItemRepository, Mockito.times(1)).deleteById(1L);
        Mockito.verifyNoMoreInteractions(mockItemRepository);
    }
}