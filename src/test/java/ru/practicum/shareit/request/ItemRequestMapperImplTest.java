package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperImplTest {

    private final ItemRequestMapper itemRequestMapper = new ItemRequestMapperImpl();

    @Test
    void dtoMapToItemRequest_whenRequestNull_thenReturnNull() {
        assertNull(itemRequestMapper.dtoMapToItemRequest(null));
    }

    @Test
    void itemRequestMapToDto_whenRequestNull_thenReturnNull() {
        assertNull(itemRequestMapper.itemRequestMapToDto(null));
    }
}