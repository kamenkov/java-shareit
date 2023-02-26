package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.user.dto.AppUserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    private AppUserDto appUserDto;

    @BeforeEach
    void setUp() {
        appUserDto = new AppUserDto();
        appUserDto.setId(1L);
    }

    @Test
    void findAll() throws Exception {
        when(userService.findAll()).thenReturn(List.of(appUserDto));

        String response = mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(appUserDto)), response);
        verify(userService, times(1)).findAll();
    }

    @Test
    void findById() throws Exception {
        when(userService.findById(anyLong())).thenReturn(appUserDto);

        String response = mvc.perform(get("/users/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(appUserDto), response);
        verify(userService, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotFound_thenThrown() throws Exception {
        when(userService.findById(anyLong())).thenThrow(new NotFoundException("message"));

        String response = mvc.perform(get("/users/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("{\"error\":\"message\"}", response);
        verify(userService, times(1)).findById(1L);
    }

    @Test
    void create() throws Exception {
        when(userService.create(any())).thenReturn(appUserDto);

        String response = mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(appUserDto), response);
        verify(userService, times(1)).create(any());
    }

    @Test
    void update() throws Exception {
        when(userService.update(anyLong(), any())).thenReturn(appUserDto);

        String response = mvc.perform(patch("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(appUserDto), response);
        verify(userService, times(1)).update(eq(1L), any());
    }

    @Test
    void removeUser() throws Exception {
        mvc.perform(delete("/users/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).removeUser(1L);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(userService);
    }
}