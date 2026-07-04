package com.tiko.tiko.Events.Repository;

import com.tiko.tiko.Events.DTO.TicketPriceDTO;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventTicketPriceRepository extends JpaRepository<EventTicketPrice, Long> {
    @Query("""
    SELECT new com.tiko.tiko.Events.DTO.TicketPriceDTO(
    etp.id,
    tt.name,
    etp.price,
    etp.quantity
    )
    FROM EventTicketPrice etp
    JOIN etp.ticketType tt
    WHERE etp.event.id = :eventid
""")
    List<TicketPriceDTO> fetchEventTicketPrices(@Param("eventId") Long eventId);
}
