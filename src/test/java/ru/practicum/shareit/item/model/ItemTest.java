package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item item1;
    private Item item2;

    @BeforeEach
    void beforeEach() {
        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setId(1L);
        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(2L);

        item1 = new Item();
        item1.setId(1L);
        item1.setItemRequest(itemRequest1);
        item1.setAvailable(true);
        item1.setName("item1");
        item1.setDescription("description1");

        item2 = new Item();
        item2.setId(1L);
        item2.setItemRequest(itemRequest2);
        item2.setAvailable(true);
        item2.setName("item2");
        item2.setDescription("description2");
    }

    @Test
    void testEquals() {
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Item{id=1, name='item1', description='description1', isAvailable=true, owner=null}",
                item1.toString());
    }
}