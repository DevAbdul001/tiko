package com.tiko.tiko.Booking.Repository;

import com.tiko.tiko.Booking.Entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository <Booking, Long> {
}
