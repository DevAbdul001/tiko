package com.tiko.tiko.Events.Service;

import com.tiko.tiko.Events.DTO.*;
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
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventsRepo eventsRepo;
    private EventCategoryRepo eventCategoryRepo;
    private TicketTypesRepo ticketTypesRepo;
    private EventTicketPriceRepository eventTicketPriceRepository;

    public List<EventCategoryResponseDTO> fetchEventCategories(){
        return  eventCategoryRepo.findAll()
                .stream()
                .map(eventCategory -> new EventCategoryResponseDTO(
                        eventCategory.getId(),
                        eventCategory.getName()
                ))
                .toList();
    }

    public List<TicketTypeResponseDTO> fetchAllTicketTypes (){
        return ticketTypesRepo.findAll()
                .stream()
                .map(ticketType -> new TicketTypeResponseDTO(
                        ticketType.getId(),
                        ticketType.getName()
                ))
                .toList();
    }


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

    public Page<EventResponseDTO> getEvents(int page){
        Pageable pageable = PageRequest.of(page, 20);

        return eventsRepo.findAllEvents(pageable);
    }


}
