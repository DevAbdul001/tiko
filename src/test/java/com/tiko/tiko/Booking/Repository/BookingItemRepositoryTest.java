package com.tiko.tiko.Booking.Repository;

import com.tiko.tiko.Booking.DTOs.BookingItemsResponseDTO;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.TestDataFactory.*;
import com.tiko.tiko.TestDataFactory.Scenerios.BookingItemScenario;
import com.tiko.tiko.TestDataFactory.Scenerios.BookingItemScenarioFactory;
import com.tiko.tiko.Users.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
public class BookingItemRepositoryTest {


    @Autowired
    BookingItemsRepository itemsRepository;

    @Autowired
    TestEntityManager em;

    @Test
    void shouldReturnBookingItemsForBooking(){
        BookingItemScenario scenario = BookingItemScenarioFactory.create(em);

        List<BookingItemsResponseDTO> list = itemsRepository.findBookingItemsByBookingId(scenario.booking().getId());
        BookingItemsResponseDTO dto = list.get(0);

        assertEquals("VIP", dto.ticketName());
        assertEquals(scenario.booking().getId(), dto.bookingId());
    }
}
