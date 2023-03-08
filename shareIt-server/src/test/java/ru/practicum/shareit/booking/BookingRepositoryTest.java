package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookingRepositoryTest {

    @Test
    void countBookingsByBookerAndItemAndStatusAndEndDateIsBefore() {
    }

    @Test
    void findBookingsByBooker() {
    }

    @Test
    void findBookingsByItem_Owner() {
    }

    @Test
    void findBookingsByBookerAndEndDateIsBefore() {
    }

    @Test
    void findBookingsByItem_OwnerAndEndDateIsBefore() {
    }

    @Test
    void findBookingsByBookerAndStartDateIsAfter() {
    }

    @Test
    void findBookingsByItem_OwnerAndStartDateIsAfter() {
    }

    @Test
    void findBookingsByBookerAndStartDateIsBeforeAndEndDateIsAfter() {
    }

    @Test
    void findBookingsByItem_OwnerAndStartDateIsBeforeAndEndDateIsAfter() {
    }

    @Test
    void findBookingsByBookerAndStatus() {
    }

    @Test
    void findBookingsByItem_OwnerAndStatus() {
    }

    @Test
    void findFirstByItem_IdAndEndDateIsBefore() {
    }

    @Test
    void findFirstByItem_IdAndStartDateIsAfter() {
    }

}