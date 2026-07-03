package com.tiko.tiko.Events.Repository;

import com.tiko.tiko.Events.Entity.EventTicketPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTicketPriceRepository extends JpaRepository<EventTicketPrice, Long> {
}
