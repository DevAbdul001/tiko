package com.tiko.tiko.Booking.Repository;

import com.tiko.tiko.Booking.DTOs.BookingResponseDTO;
import com.tiko.tiko.Booking.Entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BookingRepository extends JpaRepository <Booking, Long> {

    @Query("""
    SELECT new com.tiko.tiko.Booking.DTOs.BookingResponseDTO(
    b.id,
    b.bookingRef,
    u.name,
    e.name,
    b.totalAmount,
    b.createdAt
    )
    FROM Booking b
    JOIN b.user u
    JOIN b.event e\s
    ORDER BY b.createdAt DESC
""")
    Page<BookingResponseDTO> findBookingSummaries(Pageable pageable);
}
