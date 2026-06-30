package com.tiko.tiko.Events.Repository;

import com.tiko.tiko.Events.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventsRepo extends JpaRepository<Event, Long> {

}
