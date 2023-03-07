package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.constraints.StartBeforeEndDateValid;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.AppUserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@StartBeforeEndDateValid
public class BookingDto {

    private Long id;

    private AppUserDto booker;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @Future
    private LocalDateTime end;

    private ItemDto item;

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
