package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.AppUser;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    Integer countBookingsByBookerAndItemAndStatusAndEndDateIsBefore(AppUser user, Item item, BookingState state, LocalDateTime endDate);

    List<Booking> findBookingsByBooker(AppUser user, Sort sort);

    List<Booking> findBookingsByItem_Owner(AppUser user, Sort sort);

    /**
     * Past
     *
     * @param user
     * @param endDate
     * @return
     */
    List<Booking> findBookingsByBookerAndEndDateIsBefore(AppUser user, LocalDateTime endDate, Sort sort);

    /**
     * Past
     *
     * @param user
     * @param endDate
     * @return
     */
    List<Booking> findBookingsByItem_OwnerAndEndDateIsBefore(AppUser user, LocalDateTime endDate, Sort sort);

    /**
     * Future
     *
     * @param user
     * @param startDate
     * @return
     */
    List<Booking> findBookingsByBookerAndStartDateIsAfter(AppUser user, LocalDateTime startDate, Sort sort);

    /**
     * Future
     *
     * @param user
     * @param startDate
     * @return
     */
    List<Booking> findBookingsByItem_OwnerAndStartDateIsAfter(AppUser user, LocalDateTime startDate, Sort sort);

    /**
     * Current
     *
     * @param user
     * @param startDate
     * @param endDate
     * @return
     */
    List<Booking> findBookingsByBookerAndStartDateIsBeforeAndEndDateIsAfter(AppUser user,
                                                                            LocalDateTime startDate,
                                                                            LocalDateTime endDate,
                                                                            Sort sort);

    /**
     * Current
     *
     * @param user
     * @param startDate
     * @param endDate
     * @return
     */
    List<Booking> findBookingsByItem_OwnerAndStartDateIsBeforeAndEndDateIsAfter(AppUser user,
                                                                                LocalDateTime startDate,
                                                                                LocalDateTime endDate,
                                                                                Sort sort);

    List<Booking> findBookingsByBookerAndStatus(AppUser user, BookingState bookingState, Sort sort);

    List<Booking> findBookingsByItem_OwnerAndStatus(AppUser user, BookingState bookingState, Sort sort);

    Booking findFirstByItem_IdAndEndDateIsBefore(Long id, LocalDateTime now);

    Booking findFirstByItem_IdAndStartDateIsAfter(Long id, LocalDateTime now);

}
