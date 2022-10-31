package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    /**
     * Returns all items.
     *
     * @return {@link List} of all items or empty {@link List}.
     */
    List<Item> findAll();

    /**
     * Returns {@link Item} by the given id.
     *
     * @param id of the item to be returned.
     * @return {@link Item} wrapped in {@link Optional} or empty {@link Optional}.
     */
    Optional<Item> findById(Long id);

    /**
     * Sets the next available ID and saves the {@link Item} in storage.
     *
     * @param item the item to be saved.
     * @return saved {@link Item}.
     */
    Item createItem(Item item);

    /**
     * Saves the given {@link Item} by the given id.
     *
     * @param id of the item to be updated.
     * @param item the item to be saved.
     */
    void updateItem(Long id, Item item);

    /**
     * Checks whether there is a {@link Item} with the given id.
     *
     * @param id of the item to be checked.
     * @return true if the item exists or false.
     */
    boolean existsById(Long id);

    /**
     * Deletes the {@link Item} by the given id.
     *
     * @param id of the item to be removed.
     */
    void deleteById(Long id);
}
