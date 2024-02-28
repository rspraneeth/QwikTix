package com.qwiktix.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwiktix.data.Event;
import com.qwiktix.data.Image;
import com.qwiktix.data.User;
import com.qwiktix.data.WishItem;
import com.qwiktix.helpers.SearchHelper;
import com.qwiktix.repository.EventRepository;
import com.qwiktix.repository.ImageRepository;
import com.qwiktix.repository.WishItemRepository;
import com.qwiktix.request.NewEventRequest;
import com.qwiktix.request.SearchEventRequest;
import com.qwiktix.request.UpdateEventRequest;
import com.qwiktix.response.AdminEventResponse;
import com.qwiktix.response.EditEventResponse;
import com.qwiktix.response.ViewEventResponse;
import com.qwiktix.response.WishlistUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private WishItemRepository wishItemRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private FileServiceImpl fileService;
    @Autowired
    private SearchHelper searchHelper;

    public AdminEventResponse adminEvent() {
        try {
            List<Event> events = eventRepository.findByIsDeletedFalse();
            return new AdminEventResponse(events, events.size());
        } catch (Exception e) {
            return new AdminEventResponse(new ArrayList<>(), 0);
        }
    }

    @Transactional
    public String addNewEvent(Event event, NewEventRequest newEventRequest) {
        try {
            String fileName = fileService.uploadFile(newEventRequest.getImage());
            String url = fileService.downloadFile(fileName).toString();
            Image image = imageRepository.save(new Image(fileName,url));
            event.setImage(image);
            eventRepository.save(event);
            return "success";
        } catch (Exception e) {
            return "failed"+e.getMessage();
        }
    }

    public ViewEventResponse viewEvent(int id) {
        try {
            List<Event> events = eventRepository.findAll();
            Event currentEvent = events.stream().filter(element -> element.getId() == id).findFirst().orElse(null);
            return new ViewEventResponse(currentEvent);
        } catch (Exception e) {
            return new ViewEventResponse();
        }
    }

    public void deleteEvent(int id) {
        try {
            Event event = eventRepository.findById((long) id).orElse(null);
            assert event != null;
            Image image = imageRepository.findById(event.getImage().getId()).orElse(null);
            if (image != null){
                image.setDeleted(true);
                imageRepository.save(image);
            }
            List<WishItem> items = wishItemRepository.findByEventId(event.getId());
            if (items.size()>0){
                wishItemRepository.deleteAll(items);
            }
            event.setDeleted(true);
            eventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public EditEventResponse editEvent(int id) {
        try {
            Event event = eventRepository.findById((long) id).orElse(null);
            return new EditEventResponse(event);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void updateEvent(UpdateEventRequest updateEventRequest) {
        try {
            Event event = eventRepository.findById((long) updateEventRequest.getEventId()).orElse(null);
            if (event!=null){
                event.setEventName(updateEventRequest.getEventName());
                event.setEventDate(updateEventRequest.getEventDate());
                event.setDescription(updateEventRequest.getDescription());
                event.setCategory(updateEventRequest.getCategory());
                event.setLocation(updateEventRequest.getLocation());
                event.setTicketPrice(Double.parseDouble(updateEventRequest.getTicketPrice()));
                event.setVenue(updateEventRequest.getVenue());
//                String fileName = fileService.uploadFile(updateEventRequest.getImage());
//                String url = fileService.downloadFile(fileName).toString();
//                Image image = imageRepository.save(new Image(fileName,url));
//                event.setImage(image);
                eventRepository.save(event);
                System.out.println("event updated");
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public WishlistUserResponse userWishList(User user) {
        try {
            return new WishlistUserResponse(wishItemRepository.findByUserId(user.getId()));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void addToWishlist(Event event, User user) {
        try {
            List<WishItem> wishItem = wishItemRepository.findByUserIdAndEventId(user.getId(),event.getId());
            System.out.println("+++++++++++++++add+++++++++++++++++"+new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(wishItem));
            if (wishItem.size()==0){
                wishItemRepository.save(new WishItem(user,event));
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public AdminEventResponse filterEvents(SearchEventRequest searchEventRequest) {
        try {
            List<Event> events = eventRepository.findByIsDeletedFalse();
            List<Event> filteredEvents = events.stream()
                    .filter(event -> searchHelper.searchEvent(event, searchEventRequest)).toList();
            return new AdminEventResponse(filteredEvents, filteredEvents.size());
        } catch (Exception e) {
            return new AdminEventResponse(new ArrayList<>(), 0);
        }
    }

    public void removeFromWishlist(Event event, User user) {
        try {
            List<WishItem> wishItemList = wishItemRepository.findByUserIdAndEventId(user.getId(), event.getId());
            System.out.println("++++++++++++delete++++++++++++++++++++"+new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(wishItemList));
            if (wishItemList.size()>0){
                wishItemRepository.deleteAll(wishItemList);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


}
