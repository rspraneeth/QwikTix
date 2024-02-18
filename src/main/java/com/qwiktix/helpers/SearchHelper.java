package com.qwiktix.helpers;

import com.qwiktix.data.Event;
import com.qwiktix.request.SearchEventRequest;
import org.springframework.stereotype.Component;

@Component
public class SearchHelper {

    public boolean searchEvent(Event event, SearchEventRequest searchEventRequest) {
        String searchString = searchEventRequest.getSearchTerm().toLowerCase();
        return event.getEventName().toLowerCase().contains(searchString) ||
                event.getVenue().toLowerCase().contains(searchString) ||
                event.getDescription().toLowerCase().contains(searchString) ||
                event.getEventDate().toLowerCase().contains(searchString) ||
                event.getLocation().toLowerCase().contains(searchString) ||
                event.getCategory().toLowerCase().contains(searchString) ||
                String.valueOf(event.getTicketPrice()).toLowerCase().contains(searchString);
    }
}
