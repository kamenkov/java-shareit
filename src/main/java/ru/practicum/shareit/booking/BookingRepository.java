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


    /**
     * Returns count of {@link Booking} by given {@link AppUser} and {@link Item}.
     *
     * @param user booker.
     * @param item booked item.
     * @param state current state of booking.
     * @param endDate the {@link LocalDateTime} before the {@link Booking} ends.
     * @return count of bookings.
     */
    Integer countBookingsByBookerAndItemAndStatusAndEndDateIsBefore(AppUser user,
                                                                    Item item,
                                                                    BookingState state,
                                                                    LocalDateTime endDate);

    /**
     * Returns all {@link Booking}s by given {@link AppUser}.
     *
     * @param user booker.
     * @param sort ordering.
     * @return {@link List} of {@link Booking}.
     */
    List<Booking> findBookingsByBooker(AppUser user, Sort sort);

    /**
     * Returns all {@link Booking}s by given {@link AppUser}.
     *
     * @param user owner.
     * @param sort ordering.
     * @return {@link List} of {@link Booking}.
     */
    List<Booking> findBookingsByItem_Owner(AppUser user, Sort sort);

    /**
     * Returns {@link List} of {@link Booking} before the given {@link LocalDateTime}.
     *
     * @param user booker.
     * @param endDate end date of {@link Booking}.
     * @return {@link List} of {@link Booking} before the given {@link LocalDateTime}.
     */
    List<Booking> findBookingsByBookerAndEndDateIsBefore(AppUser user, LocalDateTime endDate, Sort sort);

    /**
     * Returns {@link List} of {@link Booking} before the given {@link LocalDateTime}.
     *
     * @param user owner.
     * @param endDate end date of {@link Booking}.
     * @return {@link List} of {@link Booking} before the given {@link LocalDateTime}.
     */
    List<Booking> findBookingsByItem_OwnerAndEndDateIsBefore(AppUser user, LocalDateTime endDate, Sort sort);

    /**
     * Returns {@link List} of {@link Booking} after the given {@link LocalDateTime}.
     *
     * @param user booker.
     * @param startDate start date of {@link Booking}.
     * @return {@link List} of {@link Booking} after the given {@link LocalDateTime}.
     */
    List<Booking> findBookingsByBookerAndStartDateIsAfter(AppUser user, LocalDateTime startDate, Sort sort);

    /**
     * Returns {@link List} of {@link Booking} after the given {@link LocalDateTime}.
     *
     * @param user owner.
     * @param startDate start date of {@link Booking}.
     * @return {@link List} of {@link Booking} after the given {@link LocalDateTime}.
     */
    List<Booking> findBookingsByItem_OwnerAndStartDateIsAfter(AppUser user, LocalDateTime startDate, Sort sort);

    /**
     * Returns {@link List} of current {@link Booking} by booker.
     *
     * @param user booker.
     * @param startDate start date of {@link Booking}.
     * @param endDate end date of {@link Booking}.
     * @return {@link List} of {@link Booking} after the {@link Booking#getStartDate()} and before {@link Booking#getEndDate()}.
     */
    List<Booking> findBookingsByBookerAndStartDateIsBeforeAndEndDateIsAfter(AppUser user,
                                                                            LocalDateTime startDate,
                                                                            LocalDateTime endDate,
                                                                            Sort sort);

    /**
     * Returns {@link List} of current {@link Booking} by owner.
     *
     * @param user owner.
     * @param startDate start date of {@link Booking}.
     * @param endDate end date of {@link Booking}.
     * @return {@link List} of {@link Booking} after the {@link Booking#getStartDate()} and before {@link Booking#getEndDate()}.
     */
    List<Booking> findBookingsByItem_OwnerAndStartDateIsBeforeAndEndDateIsAfter(AppUser user,
                                                                                LocalDateTime startDate,
                                                                                LocalDateTime endDate,
                                                                                Sort sort);

    /**
     * Returns all {@link Booking}s by given {@link AppUser} and current {@link BookingState}.
     *
     * @param user booker.
     * @param bookingState current status.
     * @param sort ordering.
     * @return {@link List} of {@link Booking} by booker.
     */
    List<Booking> findBookingsByBookerAndStatus(AppUser user, BookingState bookingState, Sort sort);

    /**
     * Returns all {@link Booking}s by given {@link AppUser} and current {@link BookingState}.
     *
     * @param user owner.
     * @param bookingState current status.
     * @param sort ordering.
     * @return {@link List} of {@link Booking} by owner.
     */
    List<Booking> findBookingsByItem_OwnerAndStatus(AppUser user, BookingState bookingState, Sort sort);

    /**
     * Returns last {@link Booking}.
     *
     * @param id booking ID.
     * @param now current {@link LocalDateTime}.
     * @return {@link Booking}.
     */
    Booking findFirstByItem_IdAndEndDateIsBefore(Long id, LocalDateTime now);

    /**
     * Returns next {@link Booking}.
     *
     * @param id booking ID.
     * @param now current {@link LocalDateTime}.
     * @return {@link Booking}.
     */
    Booking findFirstByItem_IdAndStartDateIsAfter(Long id, LocalDateTime now);

}
