package com.qwiktix.repository;

import com.qwiktix.data.Event;
import com.qwiktix.response.WishlistUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query(value = "SELECT * FROM qwiktix.event e WHERE e.event_date >= curdate() and e.is_deleted=0", nativeQuery = true)
    List<Event> findFutureEvents();

    @Query(value = "SELECT * FROM qwiktix.event e WHERE e.event_date < curdate() and e.is_deleted=0", nativeQuery = true)
    List<Event> findPastEvents();

}
