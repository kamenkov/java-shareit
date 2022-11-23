package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.AppUserDto;
import ru.practicum.shareit.user.model.AppUser;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.handler.exception.NotFoundException.notFoundException;

@Validated
@Service
public class UserService {

    public static final String USER_NOT_FOUND_MESSAGE = "User {0} not found";
    public static final String EMAIL_ALREADY_REGISTERED_MESSAGE = "User with email: %s already registered";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<AppUserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::userMapToDto).collect(Collectors.toList());
    }

    public AppUserDto findById(Long id) {
        final AppUser user = userRepository
                .findById(id)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
        return userMapper.userMapToDto(user);
    }

    public AppUser getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
    }


    public AppUserDto create(@Valid AppUserDto userDto) {
        final String email = userDto.getEmail();
        if (email == null) {
            throw new IllegalArgumentException("Email is required");
        }
        AppUser user = userMapper.dtoMapToUser(userDto);
        user = userRepository.save(user);
        return userMapper.userMapToDto(user);
    }

    public AppUserDto update(Long id, @Valid AppUserDto userDto) {
        final AppUser user = userRepository
                .findById(id)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
        final String email = userDto.getEmail();
        if (email != null) {
            user.setEmail(email);
        }
        final String name = userDto.getName();
        if (name != null) {
            user.setName(name);
        }
        userRepository.save(user);
        return userMapper.userMapToDto(user);
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

}
