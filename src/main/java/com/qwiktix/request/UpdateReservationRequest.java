package com.qwiktix.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateReservationRequest {
    private Integer reservationId;
    private Integer userId;
    private Integer eventId;
    private Integer numberOfTickets;
    private String reservationTime;
}