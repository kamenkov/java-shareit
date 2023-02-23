package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.AppUser;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingTest {

    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void beforeEach() {
        AppUser booker1 = new AppUser();
        booker1.setId(1L);
        AppUser booker2 = new AppUser();
        booker2.setId(2L);

        booking1 = new Booking();
        booking1.setId(1L);
        booking1.setBooker(booker1);

        booking2 = new Booking();
        booking2.setId(1L);
        booking2.setBooker(booker2);
    }

    @Test
    void testEquals() {
        assertEquals(booking1, booking2);
        assertEquals(booking1.hashCode(), booking2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Booking{id=1, booker=AppUser{id=1, name='null', email='null'}, " +
                "startDate=null, endDate=null, item=null, status=null}", booking1.toString());
    }
}