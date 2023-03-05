package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.AppUserDto;
import ru.practicum.shareit.user.model.AppUser;

@Mapper
public interface UserMapper {

    AppUserDto userMapToDto(AppUser user);

    AppUser dtoMapToUser(AppUserDto userDto);
}
