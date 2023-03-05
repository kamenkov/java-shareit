package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.AppUser;

import static org.junit.jupiter.api.Assertions.assertNull;

class CommentMapperTest {

    CommentMapper commentMapper;

    @BeforeEach
    void beforeEach() {
        commentMapper = new CommentMapperImpl();
    }

    @Test
    void commentMapToDto_whenCommentIsNull_thenReturnNull() {
        assertNull(commentMapper.commentMapToDto(null));
    }

    @Test
    void commentMapToDto_whenCommentAuthorIsNull_thenReturnNull() {
        Comment comment = new Comment();
        comment.setId(1L);
        assertNull(commentMapper.commentMapToDto(comment).getAuthorName());
    }

    @Test
    void commentMapToDto_whenCommentAuthorNameIsNull_thenReturnNull() {
        Comment comment = new Comment();
        comment.setId(1L);
        AppUser author = new AppUser();
        comment.setAuthor(author);
        assertNull(commentMapper.commentMapToDto(comment).getAuthorName());
    }
}