package com.qwiktix.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Event {
    @Id
    @SequenceGenerator(sequenceName = "event_sequence",name = "event_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "event_sequence")
    private long Id;
    private String eventName;
    private String eventDate;
    private String venue;
    private String description;
    private double ticketPrice;
    private String category;
    private String location;
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;
    private boolean isDeleted=false;

    public Event(String eventName, String eventDate, String venue, String description, Double ticketPrice, String category, String location) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.venue = venue;
        this.description = description;
        this.ticketPrice = ticketPrice;
        this.category = category;
        this.location = location;
    }
}
