package com.tiko.tiko.Events.Repository;

import com.tiko.tiko.Events.DTO.TicketPriceDTO;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
    WHERE etp.event.id = :eventId
""")
    List<TicketPriceDTO> fetchEventTicketPrices(@Param("eventId") Long eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EventTicketPrice e WHERE e.id = :id")
    Optional<EventTicketPrice> findByIdForUpdate(@Param("id") Long id);
}
