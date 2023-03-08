package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
    }

    @Test
    void findOwnRequests() throws Exception {
        when(itemRequestService.findOwnRequests(anyLong())).thenReturn(List.of(itemRequestDto));

        String response = mvc.perform(get("/requests")
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemRequestDto)), response);
        verify(itemRequestService, times(1)).findOwnRequests(1L);
    }

    @Test
    void findOtherRequests() throws Exception {
        when(itemRequestService.findOtherRequests(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequestDto));

        String response = mvc.perform(get("/requests/all")
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemRequestDto)), response);
        verify(itemRequestService, times(1)).findOtherRequests(1L, 0, 20);
    }

    @Test
    void findById() throws Exception {
        when(itemRequestService.findById(anyLong(), anyLong())).thenReturn(itemRequestDto);

        String response = mvc.perform(get("/requests/{id}", 1L)
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), response);
        verify(itemRequestService, times(1)).findById(1L, 1L);
    }

    @Test
    void create() throws Exception {
        when(itemRequestService.create(any(), anyLong())).thenReturn(itemRequestDto);

        String response = mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), response);
        verify(itemRequestService, times(1)).create(any(), eq(1L));

    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(itemRequestService);
    }
}