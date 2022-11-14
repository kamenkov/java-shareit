package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
}
