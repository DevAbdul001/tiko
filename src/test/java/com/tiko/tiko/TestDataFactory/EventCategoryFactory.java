package com.tiko.tiko.TestDataFactory;


import com.tiko.tiko.Events.Entity.EventCategory;

import java.util.UUID;

public final class EventCategoryFactory {

    private EventCategoryFactory() {
    }

    public static EventCategory create() {
        return new EventCategory(
                "Category-" + UUID.randomUUID()
        );
    }

    public static EventCategory create(String name) {
        return new EventCategory(name);
    }
}