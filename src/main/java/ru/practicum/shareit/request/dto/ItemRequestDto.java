package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemForItemRequestDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestDto {

    private Long id;
    @NotBlank
    private String description;

    private LocalDateTime created;

    private List<ItemForItemRequestDto> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public List<ItemForItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<ItemForItemRequestDto> items) {
        this.items = items;
    }
}
