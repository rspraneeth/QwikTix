package com.qwiktix.data;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Reservation {
    @Id
    @SequenceGenerator(sequenceName = "reservation_sequence",name = "reservation_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "reservation_sequence")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private int numberOfTickets;
    private String reservationTime;
    private boolean isDeleted=false;
    public Reservation(User user, Event event, int numberOfTickets, String reservationTime) {
        this.user = user;
        this.event = event;
        this.numberOfTickets = numberOfTickets;
        this.reservationTime = reservationTime;
    }
}