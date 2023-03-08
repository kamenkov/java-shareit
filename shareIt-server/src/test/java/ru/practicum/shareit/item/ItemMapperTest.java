package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.AppUser;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    @Test
    void itemMapToForItemRequestDto() {
        Item item = new Item();
        item.setId(1L);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        item.setItemRequest(itemRequest);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(new AppUser());

        ItemForItemRequestDto expectedDto = new ItemForItemRequestDto();
        expectedDto.setId(1L);
        expectedDto.setRequestId(1L);
        expectedDto.setAvailable(true);
        expectedDto.setName("name");
        expectedDto.setDescription("description");

        ItemMapper itemMapper = new ItemMapperImpl();
        ItemForItemRequestDto actualDto = itemMapper.itemMapToForItemRequestDto(item);

        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getName(), actualDto.getName());
        assertEquals(expectedDto.getDescription(), actualDto.getDescription());
        assertEquals(expectedDto.getRequestId(), actualDto.getRequestId());
        assertEquals(expectedDto.getAvailable(), actualDto.getAvailable());
    }
}