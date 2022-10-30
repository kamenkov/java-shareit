package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private Long lastId = 1L;

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User createUser(User user) {
        final Long id = lastId++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public void updateUser(Long id, User user) {
        users.put(id,user);
    }

    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean isEmailExists(String email) {
        return users.values().stream().anyMatch(u -> email.equals(u.getEmail()));
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

}
