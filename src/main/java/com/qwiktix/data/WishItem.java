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
public class WishItem {
    @Id
    @SequenceGenerator(sequenceName = "wish_item_sequence",name = "wish_item_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "wish_item_sequence")
    private long Id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public WishItem(User user, Event event) {
        this.user=user;
        this.event=event;
    }
}
