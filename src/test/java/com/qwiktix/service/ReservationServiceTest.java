package com.qwiktix.service;

import com.qwiktix.data.Event;
import com.qwiktix.data.Reservation;
import com.qwiktix.data.User;
import com.qwiktix.repository.EventRepository;
import com.qwiktix.repository.ReservationRepository;
import com.qwiktix.repository.UserRepository;
import com.qwiktix.response.AdminReservationResponse;
import com.qwiktix.response.UserReservationsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testAdminReservations() {
//        BigDecimal totalAmount = new BigDecimal("100.00");
//        List<Reservation> reservations = new ArrayList<>();
//        when(reservationRepository.getTotalReservationAmount()).thenReturn(totalAmount);
//        when(reservationRepository.findByIsDeletedFalse()).thenReturn(reservations);
//        AdminReservationResponse response = reservationService.adminReservations();
//        assertEquals(reservations, response.getReservations());
//        assertEquals(totalAmount.doubleValue(), response.getTotalReservationAmount());
//        assertEquals(reservations.size(), response.getNumberOfReservations());
//    }

    @Test
    void testAddNewReservationSuccess() {
        Reservation reservation = new Reservation();
        reservation.setEvent(new Event());
        reservation.setUser(new User());

        reservationService.addNewReservation(reservation);

        verify(reservationRepository, times(2)).save(any());
    }

    @Test
    void testGetUserReservations() {
        Long userId = 1L;
        List<Reservation> userReservations = new ArrayList<>();
        when(reservationRepository.findByUserId(userId)).thenReturn(userReservations);

        UserReservationsResponse response = reservationService.getUserReservations(userId);

        assertEquals(userReservations, response.getReservations());
    }

}