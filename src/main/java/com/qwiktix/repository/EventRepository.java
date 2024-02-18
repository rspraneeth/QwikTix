package com.qwiktix.repository;

import com.qwiktix.data.Event;
import com.qwiktix.response.WishlistUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByIsDeletedFalse();

}
