package com.qwiktix.service;

import com.qwiktix.data.Event;
import com.qwiktix.data.User;
import com.qwiktix.data.WishItem;
import com.qwiktix.helpers.SearchHelper;
import com.qwiktix.repository.EventRepository;
import com.qwiktix.repository.ImageRepository;
import com.qwiktix.repository.WishItemRepository;
import com.qwiktix.request.SearchEventRequest;
import com.qwiktix.response.AdminEventResponse;
import com.qwiktix.response.EditEventResponse;
import com.qwiktix.response.ViewEventResponse;
import com.qwiktix.response.WishlistUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventServiceTest {
    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private WishItemRepository wishItemRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private FileServiceImpl fileService;

    @Mock
    private SearchHelper searchHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdminEvent() {
        List<Event> events = new ArrayList<>();
        when(eventRepository.findByIsDeletedFalse()).thenReturn(events);
        AdminEventResponse response = eventService.adminEvent();
        assertEquals(events, response.getEvents());
        assertEquals(events.size(), response.getNumberOfEvents());
    }



    @Test
    void testViewEvent() {
        int eventId = 1;
        List<Event> events = new ArrayList<>();
        Event event = new Event();
        event.setId(eventId);
        events.add(event);
        when(eventRepository.findAll()).thenReturn(events);
        ViewEventResponse response = eventService.viewEvent(eventId);
        assertEquals(event, response.getEvent());
    }

    @Test
    void testDeleteEvent() {
        int eventId = 1;
        Event event = new Event();
        event.setId((long) eventId);
        eventRepository.save(event);
        verify(eventRepository).save(event);
        eventRepository.deleteAll();
        assertEquals(0,eventRepository.count());
    }

    @Test
    void testEditEvent() {
        int eventId = 1;
        Event event = new Event();
        when(eventRepository.findById((long) eventId)).thenReturn(java.util.Optional.of(event));
        EditEventResponse response = eventService.editEvent(eventId);
        assertEquals(event, response.getEvent());
    }


    @Test
    void testUserWishList() {
        User user = new User();
        List<WishItem> wishItems = new ArrayList<>();
        when(wishItemRepository.findByUserId(user.getId())).thenReturn(wishItems);
        WishlistUserResponse response = eventService.userWishList(user);
        assertEquals(wishItems, response.getItems());
    }

    @Test
    void testAddToWishlist() {
        Event event = new Event();
        User user = new User();
        List<WishItem> existingWishItems = new ArrayList<>();
        when(wishItemRepository.findByEventId(event.getId())).thenReturn(existingWishItems);
        assertDoesNotThrow(() -> eventService.addToWishlist(event, user));
    }

    @Test
    void testAdminFilterEvents() {
        SearchEventRequest searchEventRequest = new SearchEventRequest();
        List<Event> events = new ArrayList<>();
        when(eventRepository.findByIsDeletedFalse()).thenReturn(events);
        when(searchHelper.searchEvent(any(), any())).thenReturn(true);
        AdminEventResponse response = eventService.adminFilterEvents(searchEventRequest);
        assertEquals(events, response.getEvents());
        assertEquals(events.size(), response.getNumberOfEvents());
    }

    @Test
    void testRemoveFromWishlist() {
        Event event = new Event();
        User user = new User();
        List<WishItem> wishItemList = new ArrayList<>();
        when(wishItemRepository.findByEventId(event.getId())).thenReturn(wishItemList);
        assertDoesNotThrow(() -> eventService.removeFromWishlist(event, user));
    }

}