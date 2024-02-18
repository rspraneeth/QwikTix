package com.qwiktix.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwiktix.data.Event;
import com.qwiktix.data.Reservation;
import com.qwiktix.data.User;
import com.qwiktix.repository.EventRepository;
import com.qwiktix.repository.ReservationRepository;
import com.qwiktix.repository.UserRepository;
import com.qwiktix.request.UpdateReservationRequest;
import com.qwiktix.response.*;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private  EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    public AdminReservationResponse adminReservations() {
        try {
            BigDecimal sumOfAmounts = reservationRepository.getTotalReservationAmount();
            List<Reservation> reservations = reservationRepository.findByIsDeletedFalse();
            return new AdminReservationResponse(reservations,sumOfAmounts.doubleValue(),reservations.size());
        }catch (Exception e){
            return new AdminReservationResponse(new ArrayList<>(),0,0);
        }
    }

    public String addNewReservation(Reservation reservation) {
        try {
            reservation.setReservationTime(new LocalDateTime().toString());
            reservationRepository.save(reservation);
            Reservation rs = reservationRepository.save(reservation);
            String emailBody = "________________________________________________________"+"\nEvent Name : "+rs.getEvent().getEventName()
                    +"\nVenue : "+rs.getEvent().getVenue()+"\nLocation : "+rs.getEvent().getLocation()+
                    "\nDate : "+rs.getEvent().getEventDate()+"\nTotal : "+rs.getEvent().getTicketPrice()*rs.getNumberOfTickets()+
                    "\n________________________________________________________\n";
            emailService.sendEmail(reservation.getUser().getEmail(),"Booking Success",emailBody);
           return "success";
        }catch (Exception e){
            System.out.println("**************** error *************  "+e.getMessage());
            return "failed";
        }
    }

    public UserReservationsResponse getUserReservations(Long userId){
        try {
            return new UserReservationsResponse(reservationRepository.findByUserId(userId));
        }catch (Exception e){
            return new UserReservationsResponse();
        }
    }

    public ViewReservationResponse viewReservation(int id) {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            Reservation reservation = reservations.stream().filter(element -> element.getId() == id).findFirst().orElse(null);
            return new ViewReservationResponse(reservation);
        } catch (Exception e) {
            return new ViewReservationResponse();
        }
    }

    public EditReservationResponse editReservation(int id) {
        try {
            Reservation reservation = reservationRepository.findById((long) id).orElse(null);
            return new EditReservationResponse(reservation);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void updateReservation(UpdateReservationRequest updateReservationRequest) {
        try {
            Reservation reservation = reservationRepository.findById((long) updateReservationRequest.getReservationId()).orElse(null);
            if (reservation!=null){
                User user = userRepository.findById(Long.valueOf(updateReservationRequest.getUserId())).orElse(null);
                Event event = eventRepository.findById(Long.valueOf(updateReservationRequest.getEventId())).orElse(null);
                reservation.setUser(user);
                reservation.setEvent(event);
                reservation.setNumberOfTickets(updateReservationRequest.getNumberOfTickets());
                reservation.setReservationTime(updateReservationRequest.getReservationTime());
                reservationRepository.save(reservation);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void deleteReservation(int id) {
        try {
            Reservation reservation = reservationRepository.findById((long) id).orElse(null);
            assert reservation != null;
            reservation.setDeleted(true);
            reservationRepository.save(reservation);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void cancelReservation(int id) {
        try {
            Reservation reservation = reservationRepository.findById((long) id).orElse(null);
            assert reservation != null;
            reservationRepository.delete(reservation);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
