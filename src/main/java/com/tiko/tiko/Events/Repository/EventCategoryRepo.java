package com.tiko.tiko.Events.Repository;

import com.tiko.tiko.Events.Entity.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCategoryRepo extends JpaRepository<EventCategory, Long> {
}
