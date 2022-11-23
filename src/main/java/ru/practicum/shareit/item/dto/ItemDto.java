package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.practicum.shareit.booking.dto.BookingForItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ItemDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @JsonProperty("available")
    private Boolean isAvailable;

    private BookingForItemDto lastBooking;

    private BookingForItemDto nextBooking;

    List<CommentResponseDto> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public BookingForItemDto getLastBooking() {
        return lastBooking;
    }

    public void setLastBooking(BookingForItemDto lastBooking) {
        this.lastBooking = lastBooking;
    }

    public BookingForItemDto getNextBooking() {
        return nextBooking;
    }

    public void setNextBooking(BookingForItemDto nextBooking) {
        this.nextBooking = nextBooking;
    }

    public List<CommentResponseDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseDto> comments) {
        this.comments = comments;
    }
}

