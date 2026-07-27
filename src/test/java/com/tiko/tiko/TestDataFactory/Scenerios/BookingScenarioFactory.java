package com.tiko.tiko.TestDataFactory.Scenerios;

import com.tiko.tiko.Booking.Entities.Booking;
import com.tiko.tiko.Events.Entity.Event;
import com.tiko.tiko.Events.Entity.EventCategory;
import com.tiko.tiko.Events.Entity.EventTicketPrice;
import com.tiko.tiko.Events.Entity.TicketType;
import com.tiko.tiko.TestDataFactory.*;
import com.tiko.tiko.Users.Entity.User;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class BookingScenarioFactory {

    public static BookingScenario create(TestEntityManager em) {

        User organizer = UserFactory.create();
        em.persist(organizer);

        User customer = UserFactory.create();
        em.persist(customer);

        EventCategory category = EventCategoryFactory.create();
        em.persist(category);

        TicketType vip = TicketTypeFactory.create("VIP");
        em.persist(vip);

        Event event = EventFactory.create(organizer, category);
        em.persist(event);

        EventTicketPrice price =
                EventTicketPriceFactory.create(event, vip);

        em.persist(price);

        Booking booking =
                BookingFactory.create(customer, event);

        em.persist(booking);

        em.flush();

        return new BookingScenario(
                organizer,
                customer,
                category,
                vip,
                event,
                price,
                booking
        );
    }
}
