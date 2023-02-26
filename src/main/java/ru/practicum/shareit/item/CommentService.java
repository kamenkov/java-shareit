package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.AppUser;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Service
public class CommentService {

    private final UserService userService;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    public CommentService(UserService userService,
                          BookingService bookingService,
                          ItemService itemService,
                          CommentRepository commentRepository,
                          CommentMapper commentMapper) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.itemService = itemService;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public CommentResponseDto addComment(Long itemId, @Valid CommentRequestDto commentDto, Long userId) {
        AppUser author = userService.getById(userId);
        Item item = itemService.getItem(itemId);
        if (!bookingService.isUsersBookedItem(author, item)) {
            throw new IllegalArgumentException("Not booked");
        }
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        comment.setText(commentDto.getText());
        comment.setItem(item);
        commentRepository.save(comment);
        return commentMapper.commentMapToDto(comment);
    }

    public List<CommentResponseDto> getCommentsForItem(Long itemId) {
        return commentRepository.findByItem_Id(itemId).stream()
                .map(commentMapper::commentMapToDto)
                .collect(Collectors.toList());
    }
}
