package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.user.model.AppUser;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {

    @Test
    void testEquals() {
        AppUser user1 = Utils.getUser(1L);
        AppUser user2 = Utils.getUser(2L);
        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setId(1L);
        itemRequest1.setCreator(user1);
        itemRequest1.setDescription("description1");
        itemRequest1.setCreated(LocalDateTime.MAX);

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(1L);
        itemRequest2.setCreator(user2);
        itemRequest2.setDescription("description2");
        itemRequest2.setCreated(LocalDateTime.MIN);

        assertEquals(itemRequest1, itemRequest2);
        assertEquals(itemRequest1.hashCode(), itemRequest2.hashCode());
    }

    @Test
    void testToString() {
        AppUser user1 = Utils.getUser(1L);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setCreator(user1);
        itemRequest.setDescription("description1");
        itemRequest.setCreated(LocalDateTime.of(2000, 1, 1, 0, 0));

        assertEquals("ItemRequest{id=1, description='description1', " +
                "created=2000-01-01T00:00, creator=AppUser{id=1, name='Name1', email='email1@mail.ru'}}",
                itemRequest.toString());
    }
}