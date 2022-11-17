package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper
public interface CommentMapper {

    @Mapping(source = "author.name", target = "authorName")
    CommentResponseDto commentMapToDto(Comment comment);

}
