package com.tiko.tiko.Events.Repository;

import com.tiko.tiko.Events.Entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTypesRepo extends JpaRepository<TicketType, Long> {
}
