package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.DTOs.*;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Booking.Enums.IdempotencyStatus;
import com.tiko.tiko.Booking.Repository.BookingItemsRepository;
import com.tiko.tiko.Booking.Repository.BookingRepository;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.Events.Enums.EventStatus;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import com.tiko.tiko.Events.Repository.EventsRepo;
import com.tiko.tiko.Events.Repository.TicketTypesRepo;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
import com.tiko.tiko.Users.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingItemsRepository bookingItemsRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EventsRepo eventsRepo;

    @Autowired
    private EventTicketPriceRepository eventTicketPriceRepository;

    @Autowired
    private IdempotencyService idempotencyService;

    @Autowired
    private TicketTypesRepo ticketTypesRepo;


    //utility method : generates a random 36 char string
    private String generateBookingRef(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[27];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    //Entity lookups====================================================================================================
    private User getUser(Long userId){
        return userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User does not exist"));
    }

    private Event getEvent(Long eventId){
        return eventsRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    private Booking getBooking(Long bookingId){
        return  bookingRepository.findById(bookingId)
                .orElseThrow(()-> new RuntimeException("Booking not found"));
    }

    private Map<Long, EventTicketPrice> getEventTicketPrices(CreateBookingRequestDTO request) {

        List<EventTicketPrice> ticketPrices =
                eventTicketPriceRepository.findAllById(request.ticketPriceIds());

        return ticketPrices.stream()
                .collect(Collectors.toMap(
                        EventTicketPrice::getId,
                        Function.identity()
                ));
    }

    //Validation========================================================================================================
    private void validateEventIsActive(Long eventId){
        Event event = this.getEvent(eventId);
        if(event.getStatus() != EventStatus.PUBLISHED){
            throw new RuntimeException("Event is not available for booking");
        }
    }


    private void validateTicketTypeAvailability(Long eventTicketPriceId){
        EventTicketPrice eventTicketPrice = eventTicketPriceRepository.findById(eventTicketPriceId)
                .orElseThrow(()-> new RuntimeException("Invalid ticket type"));
    }

    private void validateTicketTypePriceBelongsToEvent(Long eventTicketPriceId, Long eventId){
        Optional<EventTicketPrice> eventTicketPrice = eventTicketPriceRepository.findById(eventTicketPriceId);
        if (eventTicketPrice.get().getEvent().getId() != eventId){
            throw new RuntimeException("Ticket price does not belong to event");
        }
    }

    //Creation==========================================================================================================
    private Booking createBookingEntity(CreateBookingRequestDTO requestDTO, Event event, User user){
        return new Booking(
                this.generateBookingRef(),
                0L,
                event,
                user
        );
    }

    private List<BookingItem> createBookingItemsEntity(
            CreateBookingRequestDTO requestDTO,
            Map<Long, EventTicketPrice> ticketPrices,
            Booking booking) {

        List<BookingItem> bookingItems = new ArrayList<>();

        for (BookingItemRequest itemRequest : requestDTO.items()) {

            EventTicketPrice ticketPrice =
                    ticketPrices.get(itemRequest.eventTicketPriceId());

            BookingItem item = new BookingItem(
                    itemRequest.quantity(),
                    ticketPrice.getPrice(),
                    ticketPrice.getTicketType().getName(),
                    booking,
                    ticketPrice
            );

            bookingItems.add(item);
        }

        return bookingItems;
    }

    //Calculations======================================================================================================
    private Long getTotalAmount(List<BookingItem> bookingItems){
       return bookingItems.stream()
               .mapToLong(item -> item.getUnitPrice() * item.getQuantity())
               .sum();
    }

    //Reserve ticket====================================================================================================
    @Transactional
    private  void reserveTicket(Long ticketPriceId, int quantity){

        Optional<EventTicketPrice> ticket = eventTicketPriceRepository.findByIdForUpdate(ticketPriceId);

        if (ticket.get().getRemainingTickets() < quantity) {
            throw  new RuntimeException("Not enough tickets" + ticket.get().getRemainingTickets());
        }
        ticket.get().setRemainingTickets(ticket.get().getRemainingTickets() - quantity);
    }

    //Idempotency======================================================================================================
    private IdempotencyRecord getKey(String key){
        return  idempotencyService.get(key);
    }

    public Booking createBooking(CreateBookingRequestDTO requestDTO, Long userId){
       IdempotencyRecord idempotencyRecord = this.getKey(requestDTO.idempotencyKey());

       if (idempotencyRecord != null) {
           if (idempotencyRecord.status() == IdempotencyStatus.COMPLETED){
                return this.getBooking(idempotencyRecord.bookingId());
           }
           if (idempotencyRecord.status() == IdempotencyStatus.PROCESSING){
               throw new RuntimeException("Request is already being processed.");           }
       }
       boolean started = idempotencyService.start(requestDTO.idempotencyKey());
        if (!started) {
            throw new RuntimeException("Duplicate request.");
        }

       try {

            //Entity lookup
            User user = this.getUser(userId);
            Event event = this.getEvent(requestDTO.eventId());
            Map<Long, EventTicketPrice> ticketPriceMap = this.getEventTicketPrices(requestDTO);


            //Validation
            this.validateEventIsActive(event.getId());
            for (BookingItemRequest item : requestDTO.items() ){
                this.validateTicketTypeAvailability(item.eventTicketPriceId());
            }
            for (BookingItemRequest itemRequest : requestDTO.items()){
                this.validateTicketTypePriceBelongsToEvent(itemRequest.eventTicketPriceId(), event.getId());
            }

            Booking booking = this.createBookingEntity(requestDTO, event, user);
            List<BookingItem> bookingItems = this.createBookingItemsEntity(
                    requestDTO,
                    ticketPriceMap,
                    booking
            );

            booking.setTotalAmount(this.getTotalAmount(bookingItems));
            for (BookingItemRequest itemRequest : requestDTO.items()){
                this.reserveTicket(itemRequest.eventTicketPriceId(),itemRequest.quantity());
            }

            Booking newBooking = bookingRepository.save(booking);
           assert idempotencyRecord != null;
           idempotencyService.complete(idempotencyRecord.key(), newBooking);
            return  newBooking;

    } catch (Exception e) {

           assert idempotencyRecord != null;
           idempotencyService.remove(idempotencyRecord.key());
            throw (e);
        }

        }

        public BookingDetailsResponseDTO fetchBookingById( Long bookingId, Long userId ){
                User user = this.getUser(userId);
                Booking booking = this.getBooking(bookingId);

                if (!Objects.equals(user.getId(), booking.getUser().getId())){
                    throw new RuntimeException("Not authorized to fetch booking");
                }

                BookingResponseDTO bookingResponseDTO = new BookingResponseDTO(
                        booking.getId(),
                        booking.getBookingRef(),
                        booking.getUser().getName(),
                        booking.getEvent().getName(),
                        booking.getTotalAmount(),
                        booking.getCreatedAt()
                );

                List<BookingItemsResponseDTO> bookingItemsResponseDTOS =
                        bookingItemsRepository.findBookingItemsByBookingId(booking.getId());

            return new BookingDetailsResponseDTO(
                    bookingResponseDTO, bookingItemsResponseDTOS
            );
        }

        public Page<BookingResponseDTO> getBookingSummaryForUser(Long userId, int page){
            User user = this.getUser(userId);
            Pageable pageable = PageRequest.of(page, 20);

            return bookingRepository.findUserBookingSummary(user.getId(),pageable);
        }

}
