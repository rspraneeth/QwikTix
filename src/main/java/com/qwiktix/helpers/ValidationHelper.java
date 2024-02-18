package com.qwiktix.helpers;

import com.qwiktix.data.Event;
import com.qwiktix.data.User;
import com.qwiktix.request.NewReservationRequest;
import org.springframework.stereotype.Component;

@Component
public class ValidationHelper {

    public boolean isValidRegistrationData(User user){
        if (user.getName().isEmpty()){
            return false;
        } else if (user.getUsername().isEmpty()) {
            return false;
        }else if (user.getPassword().isEmpty()) {
            return false;
        }else return !user.getEmail().isEmpty();
    }

    public boolean isValidNewEventData(Event event){
        if (event.getEventDate().isEmpty()){
            return false;
        } else if (event.getEventName().isEmpty()) {
            return false;
        }else if (event.getCategory().isEmpty()) {
            return false;
        }else if (event.getDescription().isEmpty()) {
            return false;
        }else if (event.getLocation().isEmpty()) {
            return false;
        }else if (event.getVenue().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean isValidNewReservationData(NewReservationRequest newReservationRequest) {
        if (newReservationRequest.getReservationTime().isEmpty()){
            return false;
        }
        if(newReservationRequest.getEventId()==null){
            return false;
        }
        if(newReservationRequest.getNumberOfTickets()==null){
            return false;
        }
        if(newReservationRequest.getUserId()==null){
            return false;
        }
        return true;
    }
}
