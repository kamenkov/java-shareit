package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.user.model.AppUser;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        AppUser user1 = Utils.getUser(1L);
        AppUser user2 = Utils.getUser(2L);
        Item item1 = Utils.getItem(1L, user1);
        Item item2 = Utils.getItem(2L, user2);

        comment1 = new Comment();
        comment1.setId(1L);
        comment1.setCreated(LocalDateTime.of(2020, 1, 1, 1, 1));
        comment1.setText("text");
        comment1.setAuthor(user1);
        comment1.setItem(item1);
        comment2 = new Comment();
        comment2.setId(1L);
        comment2.setCreated(LocalDateTime.of(2020, 1, 1, 0, 0));
        comment2.setText("text");
        comment2.setAuthor(user2);
        comment2.setItem(item2);
    }

    @Test
    void testEquals() {
        assertEquals(comment1, comment2);
        assertEquals(comment1.hashCode(), comment2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Comment{id=1, text='text', " +
                "author=AppUser{id=1, name='Name1', email='email1@mail.ru'}, created=2020-01-01T01:01}",
                comment1.toString());
    }
}