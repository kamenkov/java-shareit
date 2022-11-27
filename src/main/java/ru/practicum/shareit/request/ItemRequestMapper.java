package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper
public interface ItemRequestMapper {


    ItemRequest dtoMapToItemRequest(ItemRequestDto itemRequestDto);

    ItemRequestDto itemRequestMapToDto(ItemRequest itemRequest);

}
