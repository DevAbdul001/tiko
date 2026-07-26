package com.tiko.tiko.Events.Repository;

import com.tiko.tiko.Events.DTO.EventDetailsDTO;
import com.tiko.tiko.Events.DTO.EventResponseDTO;
import com.tiko.tiko.Events.Entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


public interface EventsRepo extends JpaRepository<Event, Long> {

    @Query("""
    SELECT new com.tiko.tiko.Events.DTO.EventResponseDTO(
    e.id,
    u.name,
    e.name ,
    e.date,
    e.location,
    e.imageUrl,
    e.status
    )
    FROM Event e
    JOIN e.organizer u
    ORDER BY e.date DESC
""")
    Page<EventResponseDTO> findAllEvents(Pageable pageable);

    @Query("""
    SELECT new com.tiko.tiko.Events.DTO.EventDetailsDTO(
    e.id,
    u.name,
    u.email,
     e.name ,
    e.date,
    e.location,
    e.capacity,
    e.imageUrl,
    e.status
    )
    FROM Event e
    JOIN e.organizer u
    WHERE e.id = :eventId
""")
    EventDetailsDTO findByEventId(@Param("eventId") Long eventId);

}
