package ru.practicum.shareit.user;

import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.AppUserDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    @GetMapping
    public List<AppUserDto> findAll() {
        return userService.findAll();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public AppUserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public AppUserDto create(@NotNull @RequestBody AppUserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{id}")
    public AppUserDto update(@PathVariable Long id, @NotNull @RequestBody AppUserDto userDto) {
        return userService.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Long id) {
        userService.removeUser(id);
    }

}
