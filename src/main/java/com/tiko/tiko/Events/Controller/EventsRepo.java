package com.tiko.tiko.Events.Controller;

import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Events.Entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventsRepo extends JpaRepository<Event, Long> {
    List <EventCategory> fetchEventCategories();
    List <TicketType> fetchTicket();
}
