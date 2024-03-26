package com.qwiktix.repository;

import com.qwiktix.data.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishItemRepository extends JpaRepository<WishItem,Long> {
    @Query(value = "select distinct i.id, i.event_id, i.user_id from wish_item i join event e on i.event_id=e.id where e.event_date>=current_date() and i.user_id=:id", nativeQuery = true)
    List<WishItem> findByUserId(long id);

    List<WishItem> findByEventId(long id);

    List<WishItem> findByUserIdAndEventId(Long userId, Long eventId);


}
