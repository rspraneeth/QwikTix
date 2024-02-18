package com.qwiktix.repository;

import com.qwiktix.data.Event;
import com.qwiktix.data.Reservation;
import com.qwiktix.response.UserReservationsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    @Query("SELECT COALESCE(SUM(r.numberOfTickets * e.ticketPrice), 0) FROM Reservation r JOIN r.event e WHERE r.isDeleted = false")
    BigDecimal getTotalReservationAmount();

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByIsDeletedFalse();
}
