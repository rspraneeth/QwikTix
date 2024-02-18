package com.qwiktix.response;

import com.qwiktix.data.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AdminEventResponse {
    private List<Event> events;
    private int numberOfEvents;
}
