package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    private static final String X_LATER_USER_ID = "X-Sharer-User-Id";

    @MockBean
    private BookingService mockBookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        bookingDto.setId(1L);
    }

    @Test
    void findById_withoutHeader_thenThrown() throws Exception {
        mvc.perform(get("/bookings/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_whenExists_thenReturnUser() throws Exception {
        when(mockBookingService.findById(anyLong(), anyLong())).thenReturn(bookingDto);

        String response = mvc.perform(get("/bookings/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), response);
        verify(mockBookingService, times(1)).findById(1L, 1L);
    }

    @Test
    void findAll() throws Exception {
        when(mockBookingService.findAll(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(bookingDto));

        String response = mvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), response);
        verify(mockBookingService, times(1)).findAll(1L, "ALL", 0, 20);
    }

    @Test
    void findAllForOwner() throws Exception {
        when(mockBookingService.findAllForOwner(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(bookingDto));

        String response = mvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), response);
        verify(mockBookingService, times(1)).findAllForOwner(1L, "ALL", 0, 20);
    }

    @Test
    void create() throws Exception {
        when(mockBookingService.create(any(), anyLong())).thenReturn(bookingDto);

        String response = mvc.perform(post("/bookings/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), response);
        verify(mockBookingService, times(1)).create(any(), eq(1L));
    }

    @Test
    void approve() throws Exception {
        when(mockBookingService.approve(any(), anyBoolean(), anyLong())).thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("approved", "true")
                        .header(X_LATER_USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockBookingService, times(1)).approve(1L, true, 1L);
    }
}