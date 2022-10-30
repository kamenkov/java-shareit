package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.handler.exception.EmailAlreadyInUseException;
import ru.practicum.shareit.user.model.User;

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

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::userMapToDto).collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        final User user = userRepository
                .findById(id)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
        return userMapper.userMapToDto(user);
    }

    public User getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
    }


    public UserDto create(@Valid UserDto userDto) {
        final String email = userDto.getEmail();
        if (email == null) {
            throw new IllegalArgumentException("Email is required");
        }
        if (isEmailExists(email)) {
            throw new EmailAlreadyInUseException(String.format(EMAIL_ALREADY_REGISTERED_MESSAGE, email));
        }
        User user = userMapper.dtoMapToUser(userDto);
        user = userRepository.createUser(user);
        return userMapper.userMapToDto(user);
    }

    public UserDto update(Long id, @Valid UserDto userDto) {
        final User user = userRepository
                .findById(id)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
        final String email = userDto.getEmail();
        if (email != null) {
            if (isEmailExists(email)) {
                throw new EmailAlreadyInUseException(String.format(EMAIL_ALREADY_REGISTERED_MESSAGE, email));
            }
            user.setEmail(email);
        }
        final String name = userDto.getName();
        if (name != null) {
            user.setName(name);
        }
        userRepository.updateUser(id, user);
        return userMapper.userMapToDto(user);
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    private boolean isEmailExists(String email) {
        return userRepository.isEmailExists(email);
    }
}
