package com.qwiktix.response;

import com.qwiktix.data.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ViewReservationResponse {
    Reservation reservation;
}
