package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private Long lastId = 1L;

    @Override
    public List<Item> findAll() {
        return List.copyOf(items.values());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item createItem(Item item) {
        final Long id = lastId++;
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public void updateItem(Long id, Item item) {
        items.put(id, item);
    }

    @Override
    public boolean existsById(Long id) {
        return items.containsKey(id);
    }

    @Override
    public void deleteById(Long id) {
        items.remove(id);
    }
}
