package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.AppUser;

import java.time.LocalDateTime;

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
     * @param pageable pageable.
     * @return {@link Page} of {@link Booking}.
     */
    Page<Booking> findBookingsByBooker(AppUser user, Pageable pageable);

    /**
     * Returns all {@link Booking}s by given {@link AppUser}.
     *
     * @param user owner.
     * @param pageable pageable.
     * @return {@link Page} of {@link Booking}.
     */
    Page<Booking> findBookingsByItem_Owner(AppUser user, Pageable pageable);

    /**
     * Returns {@link Page} of {@link Booking} before the given {@link LocalDateTime}.
     *
     * @param user booker.
     * @param endDate end date of {@link Booking}.
     * @return {@link Page} of {@link Booking} before the given {@link LocalDateTime}.
     */
    Page<Booking> findBookingsByBookerAndEndDateIsBefore(AppUser user, LocalDateTime endDate, Pageable pageable);

    /**
     * Returns {@link Page} of {@link Booking} before the given {@link LocalDateTime}.
     *
     * @param user owner.
     * @param endDate end date of {@link Booking}.
     * @return {@link Page} of {@link Booking} before the given {@link LocalDateTime}.
     */
    Page<Booking> findBookingsByItem_OwnerAndEndDateIsBefore(AppUser user, LocalDateTime endDate, Pageable pageable);

    /**
     * Returns {@link Page} of {@link Booking} after the given {@link LocalDateTime}.
     *
     * @param user booker.
     * @param startDate start date of {@link Booking}.
     * @return {@link Page} of {@link Booking} after the given {@link LocalDateTime}.
     */
    Page<Booking> findBookingsByBookerAndStartDateIsAfter(AppUser user, LocalDateTime startDate, Pageable pageable);

    /**
     * Returns {@link Page} of {@link Booking} after the given {@link LocalDateTime}.
     *
     * @param user owner.
     * @param startDate start date of {@link Booking}.
     * @return {@link Page} of {@link Booking} after the given {@link LocalDateTime}.
     */
    Page<Booking> findBookingsByItem_OwnerAndStartDateIsAfter(AppUser user, LocalDateTime startDate, Pageable pageable);

    /**
     * Returns {@link Page} of current {@link Booking} by booker.
     *
     * @param user booker.
     * @param startDate start date of {@link Booking}.
     * @param endDate end date of {@link Booking}.
     * @return {@link Page} of {@link Booking} after the {@link Booking#getStartDate()} and before {@link Booking#getEndDate()}.
     */
    Page<Booking> findBookingsByBookerAndStartDateIsBeforeAndEndDateIsAfter(AppUser user,
                                                                            LocalDateTime startDate,
                                                                            LocalDateTime endDate,
                                                                            Pageable pageable);

    /**
     * Returns {@link Page} of current {@link Booking} by owner.
     *
     * @param user owner.
     * @param startDate start date of {@link Booking}.
     * @param endDate end date of {@link Booking}.
     * @return {@link Page} of {@link Booking} after the {@link Booking#getStartDate()} and before {@link Booking#getEndDate()}.
     */
    Page<Booking> findBookingsByItem_OwnerAndStartDateIsBeforeAndEndDateIsAfter(AppUser user,
                                                                                LocalDateTime startDate,
                                                                                LocalDateTime endDate,
                                                                                Pageable pageable);

    /**
     * Returns all {@link Booking}s by given {@link AppUser} and current {@link BookingState}.
     *
     * @param user booker.
     * @param bookingState current status.
     * @param pageable pageable.
     * @return {@link Page} of {@link Booking} by booker.
     */
    Page<Booking> findBookingsByBookerAndStatus(AppUser user, BookingState bookingState, Pageable pageable);

    /**
     * Returns all {@link Booking}s by given {@link AppUser} and current {@link BookingState}.
     *
     * @param user owner.
     * @param bookingState current status.
     * @param pageable pageable.
     * @return {@link Page} of {@link Booking} by owner.
     */
    Page<Booking> findBookingsByItem_OwnerAndStatus(AppUser user, BookingState bookingState, Pageable pageable);

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
