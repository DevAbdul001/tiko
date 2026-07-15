package com.tiko.tiko.Booking.Repository;

import com.tiko.tiko.Booking.DTOs.BookingResponseDTO;
import com.tiko.tiko.Booking.Entities.BookingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingItemsRepository extends JpaRepository <BookingItem, Long> {

    @Query("""
    SELECT new com.tiko.tiko.Booking.DTOs.BookingItemsResponseDTO(
    bi.id,
    b.id,
    bi.ticketName,
    bi.quantity,
    bi.unitPrice
    )
    FROM BookingItem bi
    JOIN bi.booking b
    WHERE bi.bookingId = :bookingId
""")
    List<BookingResponseDTO> findBookingItemsByBookingId(@Param("bookingId") Long bookingId);
}
