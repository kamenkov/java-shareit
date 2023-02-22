package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";

    @MockBean
    ItemService itemService;

    @MockBean
    CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto;
    private CommentResponseDto commentResponseDto;
    private CommentRequestDto commentRequestDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(1L);
        commentResponseDto.setText("text");
        commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("text");
    }

    @Test
    void findAll() throws Exception {
        when(itemService.findAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemDto));

        String response = mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), response);
        verify(itemService, times(1)).findAll(1L, 0, 20);
    }

    @Test
    void findAll_whenHeaderIsEmpty_thenThrows() throws Exception {
        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_LATER_USER_ID, "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_whenHeaderIsNotExists_thenBadRequest() throws Exception {
        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_withoutHeader_thenThrown() throws Exception {
        mvc.perform(get("/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_whenExists_thenReturnUser() throws Exception {
        when(itemService.findById(anyLong(), anyLong())).thenReturn(itemDto);

        String response = mvc.perform(get("/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), response);
        verify(itemService, times(1)).findById(1L, 1L);
    }

    @Test
    void search() throws Exception {
        when(itemService.search(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemDto));

        String response = mvc.perform(get("/items/search", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "search")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), response);
        verify(itemService, times(1)).search("search", 0, 20);
    }

    @Test
    void create() throws Exception {
        when(itemService.create(any(), anyLong())).thenReturn(itemDto);

        String response = mvc.perform(post("/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), response);
        verify(itemService, times(1)).create(any(), eq(1L));
    }

    @Test
    void addComment() throws Exception {
        when(commentService.addComment(anyLong(), any(), anyLong())).thenReturn(commentResponseDto);

        String response = mvc.perform(post("/items/{id}/comment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto))
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentResponseDto), response);
        verify(commentService, times(1))
                .addComment(eq(1L), any(CommentRequestDto.class), eq(1L));
    }

    @Test
    void update() throws Exception {
        when(itemService.update(anyLong(), any(), anyLong())).thenReturn(itemDto);

        mvc.perform(patch("/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, times(1)).update(eq(1L), any(ItemDto.class), eq(1L));
    }

    @Test
    void removeItem() throws Exception {
        mvc.perform(delete("/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, times(1)).removeItem(1L, 1L);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(itemService);
    }
}