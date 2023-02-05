package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.Utils;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.user.dto.AppUserDto;
import ru.practicum.shareit.user.model.AppUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@DirtiesContext(classMode = AFTER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

    @MockBean
    UserRepository mockUserRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @Test
    void create() {
        AppUserDto appUserDto = Utils.getUserDto(1L);
        appUserDto.setEmail(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.create(appUserDto));
        appUserDto.setEmail("email@email.com");
        AppUser appUser = Utils.getUser(1L);
        userService.create(appUserDto);
        Mockito.verify(mockUserRepository, Mockito.times(1)).save(appUser);
        Mockito.verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    void update() {
        AppUserDto appUserDto = Utils.getUserDto(1L);
        Assertions.assertThrows(NotFoundException.class, () -> userService.update(1L, appUserDto));
        AppUser appUser = Utils.getUser(1L);
        Mockito.when(mockUserRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(appUser));
        userService.update(1L, appUserDto);
        Mockito.verify(mockUserRepository, Mockito.times(2)).findById(Mockito.anyLong());
        Mockito.verify(mockUserRepository, Mockito.times(1)).save(appUser);
        Mockito.verifyNoMoreInteractions(mockUserRepository);
    }


}