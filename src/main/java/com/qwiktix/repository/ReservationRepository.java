package com.qwiktix.repository;

import com.qwiktix.data.Reservation;
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

    List<Reservation> findByUserIdAndIsDeletedFalse(Long userId);

    List<Reservation> findByIsDeletedFalse();

    @Query("SELECT COALESCE(SUM(r.numberOfTickets * e.ticketPrice * 0.03), 0) FROM Reservation r JOIN r.event e WHERE r.isDeleted = false")
    BigDecimal getTotalAppProfitAmount();

    @Query(value = "select r.* from reservation r inner join event e on r.event_id = e.id where e.event_date>=curdate() and r.is_deleted=false;", nativeQuery = true)
    List<Reservation> futureReservations();

    @Query(value = "select r.* from reservation r inner join event e on r.event_id = e.id where e.event_date<curdate() and r.is_deleted=false;", nativeQuery = true)
    List<Reservation> pastReservations();

    @Query(value = "select r.* from reservation r inner join event e on r.event_id = e.id where e.event_date>=current_date() and r.user_id=:userId and r.is_deleted=false;", nativeQuery = true)
    List<Reservation> futureReservationsByUserId(Long userId);

    @Query(value = "select r.* from reservation r inner join event e on r.event_id = e.id where e.event_date<current_date() and r.user_id=:userId and r.is_deleted=false;", nativeQuery = true)
    List<Reservation> pastReservationsByUserId(Long userId);
}
