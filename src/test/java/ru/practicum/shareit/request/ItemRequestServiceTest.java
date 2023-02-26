package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@DirtiesContext(classMode = AFTER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ItemRequestServiceTest {

    private static final Sort.TypedSort<ItemRequest> itemRequestSort = Sort.sort(ItemRequest.class);
    private static final Sort CREATED_DESC_SORT = itemRequestSort.by(ItemRequest::getCreated).descending();
    @MockBean
    ItemRequestRepository itemRequestRepository;

    @MockBean
    UserService userService;

    @MockBean
    ItemService itemService;

    @Autowired
    ItemRequestMapper itemRequestMapper;

    @Autowired
    ItemRequestService itemRequestService;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = Utils.getUser(1L);
        when(userService.getById(1L)).thenReturn(user);
        ItemForItemRequestDto forItemRequestDto = new ItemForItemRequestDto();
        forItemRequestDto.setId(1L);
        when(itemService.findByRequest(anyLong())).thenReturn(List.of(forItemRequestDto));
    }

    @Test
    void findOwnRequests() {
        when(itemRequestRepository.findAllByCreator(any(), any())).thenReturn(List.of(new ItemRequest()));
        itemRequestService.findOwnRequests(1L);
        verify(itemRequestRepository, times(1)).findAllByCreator(any(), eq(CREATED_DESC_SORT));
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void findOtherRequests() {
        when(itemRequestRepository.findAllByCreatorNot(any(), any()))
                .thenReturn(new PageImpl<>(List.of(new ItemRequest())));
        itemRequestService.findOtherRequests(1L, 0, 20);
        verify(itemRequestRepository, times(1)).findAllByCreatorNot(any(), any());
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void findById() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        when(itemRequestRepository.findById(any()))
                .thenReturn(Optional.of(itemRequest));
        ItemRequestDto actual = itemRequestService.findById(1L, 1L);
        assertEquals(1L, actual.getId());
        verify(itemRequestRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void getById() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        when(itemRequestRepository.findById(any()))
                .thenReturn(Optional.of(itemRequest));
        ItemRequest actual = itemRequestService.getById(1L);
        assertEquals(1L, actual.getId());
        verify(itemRequestRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void getById_whenNotFound_thenThrown() {
        when(itemRequestRepository.findById(any()))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getById(1L));
        verify(itemRequestRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(itemRequestRepository);
    }

    @Test
    void create() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("description");
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("description");
        ItemRequestDto actualRequest = itemRequestService.create(itemRequestDto, 1L);
        assertEquals(itemRequestDto.getId(), actualRequest.getId());
        assertEquals(itemRequestDto.getDescription(), actualRequest.getDescription());
        verify(itemRequestRepository, times(1)).save(any());
        verifyNoMoreInteractions(itemRequestRepository);
    }
}