package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.AppUserDto;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

public class BookingDto {

    private Long id;

    private AppUserDto booker;

    @FutureOrPresent
    private LocalDateTime start;

    @FutureOrPresent
    private LocalDateTime end;

    private ItemDto item;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long itemId;

    private BookingState status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUserDto getBooker() {
        return booker;
    }

    public void setBooker(AppUserDto booker) {
        this.booker = booker;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public ItemDto getItem() {
        return item;
    }

    public void setItem(ItemDto item) {
        this.item = item;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BookingState getStatus() {
        return status;
    }

    public void setStatus(BookingState status) {
        this.status = status;
    }
}
