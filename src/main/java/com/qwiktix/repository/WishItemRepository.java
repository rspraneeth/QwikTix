package com.qwiktix.repository;

import com.qwiktix.data.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishItemRepository extends JpaRepository<WishItem,Long> {
    List<WishItem> findByUserId(long id);

    List<WishItem> findByEventId(long id);

    List<WishItem> findByUserIdAndEventId(Long userId, Long eventId);
}
