package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@DirtiesContext(classMode = AFTER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CommentServiceTest {

    @MockBean
    UserService mockUserService;

    @MockBean
    ItemService mockItemService;

    @MockBean
    BookingService mockBookingService;

    @MockBean
    CommentRepository mockCommentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentMapper commentMapper;

    AppUser user;
    Item item;
    Comment comment;

    @BeforeEach
    void beforeEach() {
        user = Utils.getUser(1L);
        item = Utils.getItem(1L, user);
        comment = Utils.getComment(user, "Text", item);
        Mockito.when(mockUserService.getById(1L)).thenReturn(user);
        Mockito.when(mockItemService.getItem(Mockito.anyLong())).thenReturn(item);
        Mockito.when(mockCommentRepository.save(comment)).thenReturn(comment);
        Mockito.when(mockBookingService.isUsersBookedItem(user, item)).thenReturn(true);
        Mockito.when(mockBookingService.isUsersBookedItem(null, null)).thenReturn(false);
        Mockito.when(mockUserService.getById(-1L)).thenThrow(NotFoundException.class);

    }

    @Test
    void addComment() {
        CommentRequestDto commentRequestDto = Utils.getCommentRequestDto(comment.getText());
        CommentResponseDto savedComment = commentService.addComment(1L, commentRequestDto, 1L);
        Assertions.assertEquals(comment.getText(), savedComment.getText());
        Assertions.assertEquals(comment.getAuthor().getName(), savedComment.getAuthorName());
        Mockito.verify(mockUserService, times(1)).getById(1L);
        Mockito.verify(mockItemService, times(1)).getItem(1L);
        Mockito.verify(mockCommentRepository, times(1)).save(any(Comment.class));
        Mockito.verifyNoMoreInteractions(mockUserService);
        Mockito.verifyNoMoreInteractions(mockItemService);
        Mockito.verifyNoMoreInteractions(mockCommentRepository);
    }

    @Test
    void addCommentIfNotBooked() {
        CommentRequestDto commentRequestDto = Utils.getCommentRequestDto(comment.getText());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> commentService.addComment(null, commentRequestDto, null));
    }

}