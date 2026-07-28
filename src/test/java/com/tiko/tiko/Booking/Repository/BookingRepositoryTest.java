package com.tiko.tiko.Booking.Repository;

import com.tiko.tiko.Booking.DTOs.BookingResponseDTO;
import com.tiko.tiko.TestDataFactory.Scenerios.BookingScenario;
import com.tiko.tiko.TestDataFactory.Scenerios.BookingScenarioFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    BookingRepository repository;

    @Autowired
    TestEntityManager em;

    @Test
    void shouldReturnBookingSummariesForEvent() {

        BookingScenario scenario =
                BookingScenarioFactory.create(em);

        Page<BookingResponseDTO> page =
                repository.findBookingSummaries(
                        scenario.event().getId(),
                        PageRequest.of(0, 10)
                );

        assertThat(page.getContent())
                .hasSize(1);

        BookingResponseDTO dto =
                page.getContent().get(0);

        assertThat(dto.bookingRef())
                .isEqualTo(scenario.booking().getBookingRef());

        assertThat(dto.userName())
                .isEqualTo(scenario.customer().getName());
    }

    @Test
    void shouldReturnBookingSummariesForUser(){
        BookingScenario scenario = BookingScenarioFactory.create(em);

        Page<BookingResponseDTO> page =
                repository.findUserBookingSummary(
                        scenario.customer().getId(),
                        PageRequest.of(0,20)
                );

        BookingResponseDTO dto = page.getContent().get(0);

        assertThat(dto.bookingRef())
                .isEqualTo(scenario.booking().getBookingRef());

        assertThat(dto.userName())
                .isEqualTo(scenario.customer().getName());

    }
}