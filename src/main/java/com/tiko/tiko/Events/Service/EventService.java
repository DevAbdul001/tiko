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
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventsRepo eventsRepo;

    @Autowired
    private EventCategoryRepo eventCategoryRepo;

    @Autowired
    private TicketTypesRepo ticketTypesRepo;

    @Autowired
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


    public EventDetailsDTO createEvent(CreateEventRequestDTO request, User organizer) {

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

        return new EventDetailsDTO(
                event.getId(),
                event.getName(),
                event.getDate(),
                event.getLocation(),
                event.getCapacity(),
                event.getDescription(),
                event.getStatus(),
                event.getImageUrl(),
                organizer.getName(),
                organizer.getEmail()
        );
    }

    public Page<EventResponseDTO> getEvents(int page){
        Pageable pageable = PageRequest.of(page, 20);

        return eventsRepo.findAllEvents(pageable);
    }


    public EventDetailsResponseDTO getEventById(Long id){
        eventsRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Invalid event id"));
        EventDetailsDTO event = eventsRepo.findByEventId(id);
        List<TicketPriceDTO> prices = eventTicketPriceRepository.fetchEventTicketPrices(event.id());

        return  new EventDetailsResponseDTO(
                event, prices
        );
    }


    public void updateEvent(Long eventId, UpdateEventRequest request, Long userId){
        Event event = eventsRepo.findById(eventId)
                .orElseThrow(()-> new RuntimeException("Event doesn't exist"));

        if(userId != event.getOrganizer().getId()){
            throw new RuntimeException( "You're not authorized to update this event" );
        }

        if (request.status() != null){
            event.setStatus(request.status());
        }

        if (request.location() != null) {
            event.setLocation(request.location());
        }

        if (request.date() != null){
            event.setDate(request.date());
        }

        if(request.capacity() != null){
            event.setCapacity(request.capacity());
        }
        if (request.imageUrl() != null){
            event.setImageUrl(request.imageUrl());
        }

        eventsRepo.save(event);
    }

    public void deleteEvent(Long userId, Long eventId){
        Event event = eventsRepo.findById(eventId)
                .orElseThrow(()-> new RuntimeException("Event doesnt exist"));

       if(!Objects.equals(userId, event.getOrganizer().getId())){
           throw  new RuntimeException("You're not authorized to delete this event");
       }

       eventsRepo.deleteById(eventId);
    }

}
