package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper
public interface UserMapper {

    UserDto userMapToDto(User user);

    User dtoMapToUser(UserDto userDto);
}
