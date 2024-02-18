package com.qwiktix.response;

import com.qwiktix.data.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AdminReservationResponse {
    private List<Reservation> reservations;
    private double totalReservationAmount;
    private int numberOfReservations;
}
