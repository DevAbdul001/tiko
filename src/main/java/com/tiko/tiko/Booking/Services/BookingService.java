package com.tiko.tiko.Booking.Services;

import com.tiko.tiko.Booking.Context.BookingContext;
import com.tiko.tiko.Booking.DTOs.*;
import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Booking.Entities.BookingItem;
import com.tiko.tiko.Booking.Enums.IdempotencyStatus;
import com.tiko.tiko.Booking.Repository.BookingItemsRepository;
import com.tiko.tiko.Booking.Repository.BookingRepository;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Enums.EventStatus;
import com.tiko.tiko.Events.Repository.EventTicketPriceRepository;
import com.tiko.tiko.Events.Repository.EventsRepo;
import com.tiko.tiko.Events.Repository.TicketTypesRepo;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
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
    private IdempotencyService idempotencyService;


    @Autowired
    private BookingValidationService validationService;

    @Autowired
    private BookingFactory bookingFactory;

    @Autowired
    private BookingReservationService reservationService;

    @Autowired
    private BookingContextLoader contextLoader;

    @Autowired
    private IdempotencyExecutor idempotencyExecutor;


    //Entity lookups====================================================================================================
    private User getUser(Long userId){
        return userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User does not exist"));
    }


    private Booking getBooking(Long bookingId){
        return  bookingRepository.findById(bookingId)
                .orElseThrow(()-> new RuntimeException("Booking not found"));
    }

    private BookingItem getBookingItem(Long itemId){
        return bookingItemsRepository.findById(itemId)
                .orElseThrow(()-> new RuntimeException("Invalid booking item id"));
    }


    public Booking createBooking(CreateBookingRequestDTO requestDTO, Long userId) {

        IdempotencyRecord record =
                idempotencyService.get(requestDTO.idempotencyKey());

        if (record != null) {

            if (record.status() == IdempotencyStatus.COMPLETED) {
                return getBooking(record.bookingId());
            }

            if (record.status() == IdempotencyStatus.PROCESSING) {
                throw new RuntimeException(
                        "Request is already being processed."
                );
            }
        }

        return idempotencyExecutor.execute(
                requestDTO.idempotencyKey(),
                () -> {

                    BookingContext context =
                            contextLoader.load(requestDTO.eventId());

                    User user = getUser(userId);

                    validationService.validate(requestDTO, context);

                    Booking booking =
                            bookingFactory.create(
                                    requestDTO,
                                    context,
                                    user
                            );

                    reservationService.reserveTickets(
                            booking.getBookingItems()
                    );

                    return bookingRepository.save(booking);
                }
        );
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

        public List<BookingItemsResponseDTO> updateBookingItems(UpdateBookingDTO dto, Long userId){
            User user = this.getUser(userId);
            Booking booking = this.getBooking(dto.bookingId());
            Map<BookingItem, BookingItemUpdateDTO> updates = new HashMap<>();
            List<BookingItem> bookingItems = booking.getBookingItems();

            if (!Objects.equals(booking.getUser().getId(), user.getId())){
                throw new RuntimeException("Booking doesnt belong to user");
            }

            for (BookingItemUpdateDTO itemUpdateDTO : dto.items()){
                BookingItem item = this.getBookingItem(itemUpdateDTO.itemId());

                if (!Objects.equals(item.getBooking().getId(), booking.getId())){
                    throw new RuntimeException("Booking item does not belong to booking");
                }

              updates.put(item, itemUpdateDTO);
            }


            for (Map.Entry<BookingItem, BookingItemUpdateDTO> entry : updates.entrySet()) {

                BookingItem item = entry.getKey();
                BookingItemUpdateDTO update = entry.getValue();

                item.setQuantity(update.quantity());
            }

            bookingItemsRepository.saveAll(updates.keySet());

          return bookingItemsRepository.findBookingItemsByBookingId(booking.getId());

        }

        public void deleteBooking(Long userId, Long bookingId){
            User user = this.getUser(userId);
            Booking booking = this.getBooking(bookingId);

            if (!Objects.equals(booking.getUser().getId(), user.getId())){
                throw new RuntimeException("Unauthorized action");
            }

            bookingRepository.deleteById(booking.getId());
        }


}
