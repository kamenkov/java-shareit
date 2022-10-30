package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {

    ItemDto itemMapToDto(Item item);

    Item dtoMapToItem(ItemDto itemDto);
}
