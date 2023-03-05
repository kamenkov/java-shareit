package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {

    @Mapping(source = "item.itemRequest.id", target = "requestId")
    ItemDto itemMapToDto(Item item);

    Item dtoMapToItem(ItemDto itemDto);

    @Mapping(source = "item.itemRequest.id", target = "requestId")
    ItemForItemRequestDto itemMapToForItemRequestDto(Item item);
}
