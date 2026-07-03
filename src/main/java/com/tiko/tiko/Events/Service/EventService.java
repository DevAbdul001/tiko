package com.tiko.tiko.Events.Service;

import com.tiko.tiko.Events.DTO.CreateEventRequestDTO;
import com.tiko.tiko.Events.DTO.TicketPriceRequestDTO;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.Events.Repository.EventCategoryRepo;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import com.tiko.tiko.Events.Repository.EventsRepo;
import com.tiko.tiko.Events.Repository.TicketTypesRepo;
import com.tiko.tiko.Users.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventsRepo eventsRepo;
    private EventCategoryRepo eventCategoryRepo;
    private TicketTypesRepo ticketTypesRepo;
    private EventTicketPriceRepository eventTicketPriceRepository;

    public void createEvent(CreateEventRequestDTO request, User organizer) {

        EventCategory category = eventCategoryRepo.findById(request.categoryId())
                .orElseThrow(()-> new RuntimeException("Event category doesnt exist"));

        Event event = new Event(
                request.name(),
                organizer,
                request.date(),
                category,
                request.capacity(),
                request.location()
        );

        event.setDescription(request.description());

        for (TicketPriceRequestDTO dto : request.tickets()){
            TicketType ticketType = ticketTypesRepo.findById(dto.ticketTypeId())
                    .orElseThrow(()-> new RuntimeException("Invalid ticket Type"));

            EventTicketPrice ticketPrice = new EventTicketPrice(
                    dto.price(),
                    dto.quantity(),
                    ticketType
            );

            event.addTicketPrice(ticketPrice);
        }
        eventsRepo.save(event);
    }
}
