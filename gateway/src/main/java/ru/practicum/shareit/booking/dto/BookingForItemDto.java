package ru.practicum.shareit.booking.dto;

public class BookingForItemDto {

    private Long id;
    private Long bookerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookerId() {
        return bookerId;
    }

    public void setBookerId(Long bookerId) {
        this.bookerId = bookerId;
    }
}
