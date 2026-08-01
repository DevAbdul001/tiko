package com.tiko.tiko.TestDataFactory.Scenerios;

import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.TestDataFactory.*;
import com.tiko.tiko.Users.Entity.User;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

public class EventScenarioFactory {

    public static EventScenario create(TestEntityManager em) {

        User organizer = UserFactory.create();
        em.persist(organizer);

        EventCategory category = EventCategoryFactory.create();
        em.persist(category);

        TicketType ticketType = TicketTypeFactory.create("VIP");
        em.persist(ticketType);

        Event event = EventFactory.create(organizer, category);
        em.persist(event);

        EventTicketPrice ticketPrice =
                EventTicketPriceFactory.create(event, ticketType);

        em.persist(ticketPrice);

        event.setEventTicketPriceList(List.of(ticketPrice));

        em.flush();

        return new EventScenario(
                organizer,
                category,
                ticketType,
                event,
                ticketPrice
        );
    }
}
