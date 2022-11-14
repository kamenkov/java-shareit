package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.user.model.AppUser;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerOrderByEndDate(AppUser user);

    List<Booking> findBookingsByItem_OwnerOrderByEndDate(AppUser user);

    /**
     * Past
     *
     * @param user
     * @param endDate
     * @return
     */
    List<Booking> findBookingsByBookerAndEndDateIsBeforeOrderByEndDate(AppUser user, LocalDateTime endDate);

    /**
     * Past
     *
     * @param user
     * @param endDate
     * @return
     */
    List<Booking> findBookingsByItem_OwnerAndEndDateIsBeforeOrderByEndDate(AppUser user, LocalDateTime endDate);

    /**
     * Future
     *
     * @param user
     * @param startDate
     * @return
     */
    List<Booking> findBookingsByBookerAndStartDateIsAfterOrderByEndDate(AppUser user, LocalDateTime startDate);

    /**
     * Future
     *
     * @param user
     * @param startDate
     * @return
     */
    List<Booking> findBookingsByItem_OwnerAndStartDateIsAfterOrderByEndDate(AppUser user, LocalDateTime startDate);

    /**
     * Current
     *
     * @param user
     * @param startDate
     * @param endDate
     * @return
     */
    List<Booking> findBookingsByBookerAndStartDateIsBeforeAndEndDateIsAfterOrderByEndDate(AppUser user,
                                                                                          LocalDateTime startDate,
                                                                                          LocalDateTime endDate);

    /**
     * Current
     *
     * @param user
     * @param startDate
     * @param endDate
     * @return
     */
    List<Booking> findBookingsByItem_OwnerAndStartDateIsBeforeAndEndDateIsAfterOrderByEndDate(AppUser user,
                                                                                          LocalDateTime startDate,
                                                                                          LocalDateTime endDate);

    List<Booking> findBookingsByBookerAndStatusOrderByEndDate(AppUser user, BookingState bookingState);

    List<Booking> findBookingsByItem_OwnerAndStatusOrderByEndDate(AppUser user, BookingState bookingState);


}
